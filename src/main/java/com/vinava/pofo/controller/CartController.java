package com.vinava.pofo.controller;

import com.vinava.pofo.dto.request.CartRequest;
import com.vinava.pofo.dto.request.UpdateCartStatusRequest;
import com.vinava.pofo.dto.response.CartResponse;
import com.vinava.pofo.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@RequestMapping("/pofo/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @PostMapping()
    private CartResponse createCart(@Valid @RequestBody CartRequest request,
                                    @RequestHeader(value = "X-Pofo-Client-Id") long clientId) {
        return cartService.createCart(clientId, request);
    }

    @PutMapping()
    private CartResponse updateOpenCart(@Valid @RequestBody CartRequest request,
                                        @RequestHeader(value = "X-Pofo-Client-Id") long clientId) {
        return cartService.updateCart(clientId, request);
    }

    @PutMapping("status")
    private CartResponse updateCartStatus(@Valid @RequestBody UpdateCartStatusRequest request,
                                          @RequestHeader(value = "X-Pofo-Client-Id") long clientId) {
        return cartService.updateCartStatus(clientId, request);
    }

    @DeleteMapping("{id}")
    private boolean deleteCart(@NotNull @PathVariable(value = "id") Long userId,
                                   @RequestHeader(value = "X-Pofo-Client-Id") long clientId) {
        return cartService.deleteCart(clientId, userId);
    }

    @GetMapping("{id}")
    private CartResponse getCart(@NotNull @PathVariable(value = "id") Long cartId,
                                     @RequestHeader(value = "X-Pofo-Client-Id") long clientId) {
        return cartService.getCart(clientId, cartId);
    }

    @GetMapping("open/{id}")
    private CartResponse getOpenCart(@NotNull @PathVariable(value = "id") Long userId,
                                         @RequestHeader(value = "X-Pofo-Client-Id") long clientId) {
        return cartService.getOpenCart(clientId, userId);
    }

    @GetMapping()
    public ResponseEntity<List<CartResponse>> getAllCarts(@RequestParam(value = "_page_number", defaultValue = "0") Integer pageNumber,
                                                          @RequestParam(value = "_page_size", defaultValue = "10") Integer pageSize,
                                                          @RequestParam(value = "_sort_by", defaultValue = "id") String sortBy,
                                                          @RequestParam(value = "_order", defaultValue = "DESC") String order,
                                                          @RequestHeader(value = "X-Pofo-Client-Id") long clientId) {
        return cartService.getAllCarts(clientId, pageNumber, pageSize, sortBy, order);
    }

}
