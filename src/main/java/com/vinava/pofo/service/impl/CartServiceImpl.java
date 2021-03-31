package com.vinava.pofo.service.impl;

import com.vinava.pofo.dao.CartRepository;
import com.vinava.pofo.dto.request.CartRequest;
import com.vinava.pofo.dto.request.UpdateCartStatusRequest;
import com.vinava.pofo.dto.response.CartResponse;
import com.vinava.pofo.dto.response.StockResponse;
import com.vinava.pofo.enumeration.CartStatus;
import com.vinava.pofo.exception.ProcessException;
import com.vinava.pofo.model.Cart;
import com.vinava.pofo.model.embed.CartEntity;
import com.vinava.pofo.service.CartService;
import com.vinava.pofo.service.StockService;
import com.vinava.pofo.util.ComputationUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class CartServiceImpl implements CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private StockService stockService;

    @Override
    public CartResponse createCart(long clientId, CartRequest cartRequest) {
        log.debug("Creating new cart with cart request: {}", cartRequest);
        if (cartRequest.getUserId() != null) {
            Optional<Cart> optionalExistingCart =
                    cartRepository.findByClientIdAndUserIdAndCartStatus(clientId, cartRequest.getUserId(), CartStatus.OPEN);
            if (optionalExistingCart.isPresent()) {
                log.error("Existing open cart present for clientId: {}, userId: {}, cart: {}",
                        clientId, cartRequest.getUserId(), optionalExistingCart.get());
                String errorMessage = String.format("Open cart is present for clientId: %s and userId: %s",
                        clientId, cartRequest.getUserId());
                throw new ProcessException("Cart creation", errorMessage);
            }
        }
        validateCartEntities(cartRequest.getCartEntities(), clientId);
        Cart cart = cartRequest.from(clientId);
        cart.setCartStatus(CartStatus.OPEN);
        cart = cartRepository.save(cart);
        log.debug("Cart created: {}", cart);
        return CartResponse.from(cart, stockService);
    }

    @Override
    public CartResponse updateCart(long clientId, CartRequest cartRequest) {
        log.debug("Updating cart with cart request: {}", cartRequest);
        if (cartRequest.getCartEntities() == null || cartRequest.getCartEntities().size() == 0) {
            log.debug("Empty cart entities, deleting cart");
            deleteCart(clientId, cartRequest.getUserId());
            return null;
        }
        Optional<Cart> optionalExistingCart =
                cartRepository.findByClientIdAndUserIdAndCartStatus(clientId, cartRequest.getUserId(), CartStatus.OPEN);
        if (!optionalExistingCart.isPresent()) {
            log.error("No existing open cart present for clientId: {}, userId: {}", clientId, cartRequest.getUserId());
            String errorMessage = String.format("No open cart is present for clientId: %s and userId: %s",
                    clientId, cartRequest.getUserId());
            throw new ProcessException("Cart updation", errorMessage);
        }
        Cart cart = cartRequest.from(clientId);
        cart.setCartStatus(CartStatus.OPEN);
        cart = cartRepository.save(cart);
        log.debug("Cart updated: {}", cart);
        return CartResponse.from(cart, stockService);
    }

    @Override
    public boolean deleteCart(long clientId, long userId) {
        log.debug("Deleting open cart with clientId: {}, userId: {}", clientId, userId);
        Optional<Cart> optionalExistingCart =
                cartRepository.findByClientIdAndUserIdAndCartStatus(clientId, userId, CartStatus.OPEN);
        if (!optionalExistingCart.isPresent()) {
            log.error("No existing open cart present for clientId: {}, userId: {}", clientId, userId);
            String errorMessage = String.format("No open cart is present for clientId: %s and userId: %s", clientId, userId);
            throw new ProcessException("Cart deletion", errorMessage);
        }
        Cart cart = optionalExistingCart.get();
        cartRepository.delete(cart);
        return true;
    }

    @Override
    public CartResponse getCart(long clientId, long cartId) {
        log.debug("Fetching cart with clientId: {}, cartId: {}", clientId, cartId);
        Optional<Cart> optionalExistingCart =
                cartRepository.findByClientIdAndId(clientId, cartId);
        if (!optionalExistingCart.isPresent()) {
            log.error("No existing cart present for clientId: {}, cartId: {}", clientId, cartId);
            String errorMessage = String.format("No cart is present for clientId: %s and cartId: %s", clientId, cartId);
            throw new ProcessException("Cart fetch", errorMessage);
        }
        Cart cart = optionalExistingCart.get();
        return CartResponse.from(cart, stockService);
    }

    @Override
    public CartResponse getOpenCart(long clientId, long userId) {
        log.debug("Fetching open cart with clientId: {}, userId: {}", clientId, userId);
        Optional<Cart> optionalExistingCart =
                cartRepository.findByClientIdAndUserIdAndCartStatus(clientId, userId, CartStatus.OPEN);
        if (!optionalExistingCart.isPresent()) {
            log.error("No existing open cart present for clientId: {}, userId: {}", clientId, userId);
            String errorMessage = String.format("No open cart is present for clientId: %s and userId: %s", clientId, userId);
            throw new ProcessException("Cart fetch", errorMessage);
        }
        Cart cart = optionalExistingCart.get();
        return CartResponse.from(cart, stockService);
    }

    @Override
    public ResponseEntity<List<CartResponse>> getAllCarts(Long clientId, Integer pageNumber,
                                                   Integer pageSize, String sortBy, String order) {
        log.debug("Starting getAllCarts with pageNumber: {}, pageSize: {}, sortBy: {}, order: {} and clientId: {}",
                pageNumber, pageSize, sortBy, order, clientId);
        Sort.Direction direction = (order.equals("ASC")) ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(direction, sortBy));
        List<Cart> carts = cartRepository.findAllByClientId(clientId, pageable);
        log.debug("Returning from getAllCarts for clientId: {} with response: {}", clientId, carts);
        return CartResponse.getResponseEntityFrom(carts, stockService);
    }

    @Override
    public CartResponse updateCartStatus(long clientId, UpdateCartStatusRequest request) {
        log.debug("Updating cart status fot clientId: {}, and updateCartStatusRequest: {}", clientId, request);
        Optional<Cart> optionalCart = cartRepository.findByClientIdAndId(clientId, request.getCartId());
        if (!optionalCart.isPresent()) {
            log.error("No existing cart present for clientId: {}, id: {}", clientId, request.getCartId());
            String errorMessage = String.format("No cart is present for clientId: %s with id: %s", clientId, request.getCartId());
            throw new ProcessException("Cart status update", errorMessage);
        }
        Cart cart = optionalCart.get();
        if (CartStatus.CLOSED.equals(cart.getCartStatus())) {
            log.error("Cart update is not allowed in closed cart");
            throw new ProcessException("Cart status update", "Cart status update not allowed in closed cart");
        } else if (cart.getCartStatus().getValue() >= request.getCartStatus().getValue()) {
            log.error("Status update is invalid");
            throw new ProcessException("Cart status update", "Invalid cart update status");
        }
        cart.setCartStatus(request.getCartStatus());
        cart = cartRepository.save(cart);
        log.debug("Cart status updated: {}", cart);
        return CartResponse.from(cart, stockService);
    }

    private void validateCartEntities(List<CartEntity> cartEntities, long clientId) {
        String processName = "Cart creation/updation";
        if (cartEntities == null || cartEntities.size() == 0) {
            throw new ProcessException(processName, "Cart entities are empty");
        }
        log.debug("Checking if valid products present in cart entity");
        StockResponse stockResponse;
        for (CartEntity cartEntity : cartEntities) {
            try {
                stockResponse = stockService.getStockById(cartEntity.getStockId(), clientId);
            } catch (Exception e) {
                log.error("Invalid stock id: {} for clientId: {}", cartEntity.getStockId(), clientId);
                throw new ProcessException(processName, "Invalid stock id in cart entity");
            }
            log.debug("Valid stock fetched: {}", stockResponse);
            if (!ComputationUtil.isValidPercentage(cartEntity.getDiscountPercentage())) {
                log.error("Invalid discount percentage for stockId: {}, clientId: {}", cartEntity.getStockId(), clientId);
                throw new ProcessException(processName, "Invalid discount percentage");
            } else if (!ComputationUtil.isValidPercentage(cartEntity.getGstPercentage())) {
                log.error("Invalid gst percentage for stockId: {}, clientId: {}", cartEntity.getStockId(), clientId);
                throw new ProcessException(processName, "Invalid tax percentage");
            } else if (cartEntity.getQuantity() == null || cartEntity.getQuantity().compareTo(BigDecimal.ZERO) < 1) {
                log.error("Invalid quantity for stockId: {}, clientId: {}", cartEntity.getStockId(), clientId);
                throw new ProcessException(processName, "Invalid quantity mention, quantity should be > 0");
            }
        }
    }

}
