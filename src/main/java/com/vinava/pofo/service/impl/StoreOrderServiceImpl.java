package com.vinava.pofo.service.impl;

import com.vinava.pofo.dao.StoreOrderRepository;
import com.vinava.pofo.dto.request.StockMovementRequest;
import com.vinava.pofo.dto.request.StoreOrderRequest;
import com.vinava.pofo.dto.response.CartResponse;
import com.vinava.pofo.dto.response.StoreOrderResponse;
import com.vinava.pofo.enumeration.OrderStatus;
import com.vinava.pofo.enumeration.StockMovementReferenceType;
import com.vinava.pofo.enumeration.StockMovementType;
import com.vinava.pofo.exception.ProcessException;
import com.vinava.pofo.model.StoreOrder;
import com.vinava.pofo.service.CartService;
import com.vinava.pofo.service.StockMovementService;
import com.vinava.pofo.service.StoreOrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class StoreOrderServiceImpl implements StoreOrderService {

    @Autowired
    private StoreOrderRepository storeOrderRepository;

    @Autowired
    private CartService cartService;

    @Autowired
    private StockMovementService stockMovementService;

    @Override
    public StoreOrderResponse createStoreOrder(StoreOrderRequest storeOrderRequest, long clientId) {
        log.debug("Starting createStoreOrder with request: {}, for clientId: {}", storeOrderRequest, clientId);
        storeOrderRequest.validateStoreOrderRequest();
        long cartId;
        CartResponse cartResponse = null;
        OrderStatus orderStatus;
        if (storeOrderRequest.getCartId() == null || storeOrderRequest.getCartId() == 0L) {
            cartResponse = cartService.createCart(clientId, storeOrderRequest.getCartRequest());
            orderStatus = OrderStatus.PROCESSING;
        } else {
            cartId = storeOrderRequest.getCartId();
            Optional<StoreOrder> optionalStoreOrder = storeOrderRepository.findByCartIdAndClientId(
                    cartId, clientId);
            if (optionalStoreOrder.isPresent()) {
                log.error("StoreOrder already exists with cartId: {} for clientId: {}",
                        storeOrderRequest.getCartId(), clientId);
                throw new ProcessException("StoreOrder creation", "StoreOrder already exists with same name");
            }
            orderStatus = OrderStatus.RECEIVED;
        }
        Optional<StoreOrder> optionalStoreOrderSlug = storeOrderRepository.findByOrderSlugAndClientId(
                storeOrderRequest.getOrderSlug(), clientId);
        if (optionalStoreOrderSlug.isPresent()) {
            log.error("StoreOrder already exists with orderSlug: {} for clientId: {}",
                    storeOrderRequest.getOrderSlug(), clientId);
            throw new ProcessException("StoreOrder creation", "StoreOrder already exists with same order slug");
        }
        StoreOrder storeOrder = storeOrderRequest.from(clientId, cartResponse);
        storeOrder.setOrderStatus(orderStatus);
        storeOrder = storeOrderRepository.save(storeOrder);
        StoreOrderResponse storeOrderResponse = StoreOrderResponse.from(storeOrder, cartService);
        createStockMovement(storeOrderResponse, false);
        log.debug("Returning from createStoreOrder with response: {}, for clientId: {}", storeOrder, clientId);
        return storeOrderResponse;
    }

    private void createStockMovement(StoreOrderResponse order, boolean cancelledOrder) {
        for (CartResponse.CartEntityResponse cartEntityResponse : order.getCart().getCartEntityResponses()) {
            StockMovementRequest stockMovementRequest = getStockMovementRequest(cartEntityResponse, order, cancelledOrder);
            stockMovementService.createStockMovement(stockMovementRequest, order.getClientId(), true);
        }
    }

    private StockMovementRequest getStockMovementRequest
            (CartResponse.CartEntityResponse cartEntityResponse, StoreOrderResponse storeOrderResponse, boolean cancelledOrder) {
        return StockMovementRequest.builder()
                .quantity(cartEntityResponse.getQuantity())
                .stockMovementType(cancelledOrder ? StockMovementType.IN : StockMovementType.OUT)
                .referenceId(storeOrderResponse.getOrderSlug())
                .comment(cancelledOrder ? "Vendor Order Cancellation" : "Vendor Order")
                .stockMovementReferenceType(cancelledOrder ? StockMovementReferenceType.ORDER_RETURN : StockMovementReferenceType.ORDER)
                .storeId(storeOrderResponse.getStoreId())
                .stockId(cartEntityResponse.getStock().getId())
                .build();
    }

    @Override
    public StoreOrderResponse updateStoreOrder(StoreOrderRequest storeOrderRequest, long id, long clientId) {
        log.debug("Updating storeOrder with request: {}, id: {}, for clientId: {}", storeOrderRequest, id, clientId);
        Optional<StoreOrder> optionalStoreOrder = storeOrderRepository.findByIdAndClientId(id, clientId);
        if (!optionalStoreOrder.isPresent()) {
            log.error("StoreOrder not present with id: {}, and clientId: {}", id, clientId);
            throw new ProcessException("StoreOrder updation", "Invalid storeOrder ID");
        }
        StoreOrder storeOrder = storeOrderRequest.from(clientId, null);
        storeOrder.setId(id);
        storeOrder = storeOrderRepository.save(storeOrder);
        log.debug("Updated storeOrder with id: {} and clientId: {}. Response: {}", id, clientId, storeOrder);
        return StoreOrderResponse.from(storeOrder, cartService);
    }

    @Override
    public StoreOrderResponse updateStoreOrderStatus(long id, OrderStatus orderStatus, long clientId) {
        log.debug("Updating storeOrder with orderStatus: {}, id: {}, for clientId: {}", orderStatus, id, clientId);
        Optional<StoreOrder> optionalStoreOrder = storeOrderRepository.findByIdAndClientId(id, clientId);
        if (!optionalStoreOrder.isPresent()) {
            log.error("StoreOrder not present with id: {}, and clientId: {}", id, clientId);
            throw new ProcessException("StoreOrder updation", "Invalid storeOrder ID");
        }
        StoreOrder storeOrder = optionalStoreOrder.get();
        storeOrder.setOrderStatus(orderStatus);
        storeOrder = storeOrderRepository.save(storeOrder);
        StoreOrderResponse storeOrderResponse = StoreOrderResponse.from(storeOrder, cartService);
        if (OrderStatus.CANCELLED.equals(orderStatus)) {
            createStockMovement(storeOrderResponse, true);
        }
        log.debug("Updated storeOrder with id: {}, orderStatus: {} and clientId: {}. Response: {}", id, orderStatus, clientId, storeOrder);
        return storeOrderResponse;
    }

    @Override
    public boolean deleteStoreOrder(long id, long clientId) {
        log.debug("Deleting storeOrder with id: {}, for clientId: {}", id, clientId);
        Optional<StoreOrder> optionalStoreOrder = storeOrderRepository.findByIdAndClientId(id, clientId);
        if (!optionalStoreOrder.isPresent()) {
            log.error("StoreOrder not present with id: {}, and clientId: {}", id, clientId);
            throw new ProcessException("StoreOrder deletion", "Invalid storeOrder ID");
        }
        StoreOrder storeOrder = optionalStoreOrder.get();
        storeOrderRepository.delete(storeOrder);
        log.debug("Deleted storeOrder with id: {} and clientId: {}", id, clientId);
        return true;
    }

    @Override
    public StoreOrderResponse getStoreOrderById(long id, long clientId) {
        log.debug("Fetching storeOrder with id: {}, for clientId: {}", id, clientId);
        Optional<StoreOrder> optionalStoreOrder = storeOrderRepository.findByIdAndClientId(id, clientId);
        if (!optionalStoreOrder.isPresent()) {
            log.error("StoreOrder not present with id: {}, and clientId: {}", id, clientId);
            throw new ProcessException("Get storeOrder by id", "Invalid storeOrder ID");
        }
        StoreOrder storeOrder = optionalStoreOrder.get();
        log.debug("Fetched storeOrder with id: {} and clientId: {}. Response: {}", id, clientId, storeOrder);
        return StoreOrderResponse.from(storeOrder, cartService);
    }

    @Override
    public ResponseEntity<List<StoreOrderResponse>> getAllStoreOrders(long clientId, long storeId, Integer pageNumber,
                                                                      Integer pageSize, String sortBy, String order) {
        log.debug("Starting getAllStoreOrders with pageNumber: {}, pageSize: {}, sortBy: {}, order: {} " +
                ", clientId: {}, storeId: {}", pageNumber, pageSize, sortBy, order, clientId, storeId);
        Sort.Direction direction = (order.equals("ASC")) ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(direction, sortBy));
        List<StoreOrder> storeOrders = (storeId == 0L) ?
                storeOrderRepository.findAllByClientId(clientId, pageable)
                : storeOrderRepository.findAllByClientIdAndStoreId(clientId, storeId, pageable);
        log.debug("Returning from getAllStoreOrders for clientId: {}, storeId: {} with response: {}",
                clientId, storeId, storeOrders);
        return StoreOrderResponse.getResponseEntityFrom(storeOrders, cartService);
    }

}
