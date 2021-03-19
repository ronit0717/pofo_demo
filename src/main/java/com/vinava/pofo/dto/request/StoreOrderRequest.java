package com.vinava.pofo.dto.request;

import com.vinava.pofo.enumeration.OrderType;
import com.vinava.pofo.model.StoreOrder;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@Builder
public class StoreOrderRequest {

    @NotNull
    private Long storeId;

    @NotNull
    private Long cartId;

    @NotNull
    private Long userId;

    @NotNull
    private OrderType orderType;

    public StoreOrder from(long clientId) {
        return StoreOrder.builder()
                .clientId(clientId)
                .storeId((this.storeId))
                .cartId(this.cartId)
                .userId(this.userId)
                .orderType(this.orderType)
                .build();
    }

}
