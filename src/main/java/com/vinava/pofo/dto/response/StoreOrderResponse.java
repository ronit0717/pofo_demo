package com.vinava.pofo.dto.response;

import com.vinava.pofo.enumeration.OrderStatus;
import com.vinava.pofo.enumeration.OrderType;
import com.vinava.pofo.model.StoreOrder;
import com.vinava.pofo.service.CartService;
import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

import java.util.*;

@Data
@Builder
@Slf4j
public class StoreOrderResponse {

    private long id;
    private long clientId;
    private long storeId;
    private Long userId;
    private String orderSlug;
    private CartResponse cart;
    private OrderType orderType;
    private OrderStatus orderStatus;
    private Integer userRating;
    private Date createdOn;
    private Date updatedOn;

    public static StoreOrderResponse from(StoreOrder storeOrder, CartService cartService) {
        CartResponse cartResponse = cartService.getCart(storeOrder.getClientId(), storeOrder.getCartId());
        return StoreOrderResponse.builder()
                .id(storeOrder.getId())
                .clientId(storeOrder.getClientId())
                .cart(cartResponse)
                .userId(storeOrder.getUserId())
                .orderSlug(storeOrder.getOrderSlug())
                .storeId(storeOrder.getStoreId())
                .orderType(storeOrder.getOrderType())
                .orderStatus(storeOrder.getOrderStatus())
                .userRating(storeOrder.getUserRating())
                .createdOn(storeOrder.getCreatedOn())
                .updatedOn(storeOrder.getUpdatedOn())
                .build();
    }

    private static List<StoreOrderResponse> from(List<StoreOrder> storeOrders, CartService cartService) {
        List<StoreOrderResponse> storeOrderResponses = new LinkedList<>();
        for (StoreOrder storeOrder : storeOrders) {
            storeOrderResponses.add(from(storeOrder, cartService));
        }
        return storeOrderResponses;
    }

    public static ResponseEntity<List<StoreOrderResponse>> getResponseEntityFrom(List<StoreOrder> storeOrders, CartService cartService) {
        try {
            List<StoreOrderResponse> storeOrderResponses = from(storeOrders, cartService);
            HttpHeaders headers = new HttpHeaders();
            headers.add("X-Total-Count", String.valueOf(storeOrderResponses.size()));
            headers.add("Access-Control-Expose-Headers", "X-Total-Count");

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(storeOrderResponses);
        } catch (Exception e) {
            log.error("In exception block of getResponseEntityFrom for list of store orders: {}", storeOrders, e);
            return ResponseEntity.badRequest().build();
        }
    }

}
