package com.vinava.pofo.service;

import com.vinava.pofo.dto.request.CartRequest;
import com.vinava.pofo.dto.request.UpdateCartStatusRequest;
import com.vinava.pofo.dto.response.CartResponse;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface CartService {
    CartResponse createCart(long clientId, CartRequest cartRequest);
    CartResponse updateCart(long clientId, CartRequest cartRequest);
    boolean deleteCart(long clientId, long userId);
    CartResponse getCart(long clientId, long cartId);
    CartResponse getOpenCart(long clientId, long userId);
    ResponseEntity<List<CartResponse>> getAllCarts(Long clientId, Integer pageNumber,
                                                        Integer pageSize, String sortBy, String order);
    CartResponse updateCartStatus(long clientId, UpdateCartStatusRequest request);
}
