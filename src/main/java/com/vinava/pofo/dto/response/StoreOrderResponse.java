package com.vinava.pofo.dto.response;

import com.vinava.pofo.enumeration.OrderStatus;
import com.vinava.pofo.enumeration.OrderType;
import com.vinava.pofo.model.StoreOrder;
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
    private long userId;
    private String orderSlug;
    private long cartId;
    private OrderType orderType;
    private OrderStatus orderStatus;
    private Integer userRating;
    private Date createdOn;
    private Date updatedOn;

    public static StoreOrderResponse from(StoreOrder storeOrder) {
        return StoreOrderResponse.builder()
                .id(storeOrder.getId())
                .clientId(storeOrder.getClientId())
                .cartId(storeOrder.getCartId())
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

    private static List<StoreOrderResponse> from(List<StoreOrder> storeOrders) {
        List<StoreOrderResponse> storeOrderResponses = new LinkedList<>();
        for (StoreOrder storeOrder : storeOrders) {
            storeOrderResponses.add(from(storeOrder));
        }
        return storeOrderResponses;
    }

    public static ResponseEntity<List<StoreOrderResponse>> getResponseEntityFrom(List<StoreOrder> storeOrders) {
        try {
            List<StoreOrderResponse> storeOrderResponses = from(storeOrders);
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
