package com.vinava.pofo.service.impl;

import com.vinava.pofo.dao.StoreOrderRepository;
import com.vinava.pofo.dto.request.StoreOrderRequest;
import com.vinava.pofo.dto.response.StoreOrderResponse;
import com.vinava.pofo.exception.ProcessException;
import com.vinava.pofo.model.StoreOrder;
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

    @Override
    public StoreOrderResponse createStoreOrder(StoreOrderRequest storeOrderRequest, long clientId) {
        log.debug("Starting createStoreOrder with request: {}, for clientId: {}", storeOrderRequest, clientId);
        Optional<StoreOrder> optionalStoreOrder = storeOrderRepository.findByCartIdAndClientId(
                storeOrderRequest.getCartId(), clientId);
        if (optionalStoreOrder.isPresent()) {
            log.error("StoreOrder already exists with cartId: {} for clientId: {}",
                    storeOrderRequest.getCartId(), clientId);
            throw new ProcessException("StoreOrder creation", "StoreOrder already exists with same name");
        }
        Optional<StoreOrder> optionalStoreOrderSlug = storeOrderRepository.findByOrderSlugAndClientId(
                storeOrderRequest.getOrderSlug(), clientId);
        if (optionalStoreOrderSlug.isPresent()) {
            log.error("StoreOrder already exists with orderSlug: {} for clientId: {}",
                    storeOrderRequest.getOrderSlug(), clientId);
            throw new ProcessException("StoreOrder creation", "StoreOrder already exists with same order slug");
        }
        StoreOrder storeOrder = storeOrderRequest.from(clientId);
        storeOrder = storeOrderRepository.save(storeOrder);
        log.debug("Returning from createStoreOrder with response: {}, for clientId: {}", storeOrder, clientId);
        return StoreOrderResponse.from(storeOrder);
    }

    @Override
    public StoreOrderResponse updateStoreOrder(StoreOrderRequest storeOrderRequest, long id, long clientId) {
        log.debug("Updating storeOrder with request: {}, id: {}, for clientId: {}", storeOrderRequest, id, clientId);
        Optional<StoreOrder> optionalStoreOrder = storeOrderRepository.findByIdAndClientId(id, clientId);
        if (!optionalStoreOrder.isPresent()) {
            log.error("StoreOrder not present with id: {}, and clientId: {}", id, clientId);
            throw new ProcessException("StoreOrder updation", "Invalid storeOrder ID");
        }
        StoreOrder storeOrder = storeOrderRequest.from(clientId);
        storeOrder.setId(id);
        storeOrder = storeOrderRepository.save(storeOrder);
        log.debug("Updated storeOrder with id: {} and clientId: {}. Response: {}", id, clientId, storeOrder);
        return StoreOrderResponse.from(storeOrder);
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
        return StoreOrderResponse.from(storeOrder);
    }

    @Override
    public ResponseEntity<List<StoreOrderResponse>> getAllStoreOrders(long clientId, Integer pageNumber,
                                                                      Integer pageSize, String sortBy, String order) {
        log.debug("Starting getAllStoreOrders with pageNumber: {}, pageSize: {}, sortBy: {}, order: {} " +
                ", clientId: {}", pageNumber, pageSize, sortBy, order, clientId);
        Sort.Direction direction = (order.equals("ASC")) ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(direction, sortBy));
        List<StoreOrder> storeOrders = storeOrderRepository.findAllByClientId(clientId, pageable);
        log.debug("Returning from getAllStoreOrders for clientId: {} with response: {}",
                clientId, storeOrders);
        return StoreOrderResponse.getResponseEntityFrom(storeOrders);
    }

}
