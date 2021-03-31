package com.vinava.pofo.dto.request;

import com.vinava.pofo.dto.response.CartResponse;
import com.vinava.pofo.enumeration.OrderType;
import com.vinava.pofo.exception.ProcessException;
import com.vinava.pofo.model.StoreOrder;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Builder
public class StoreOrderRequest {

    @NotNull
    private Long storeId;

    private Long cartId;

    private CartRequest cartRequest;

    private Long userId;

    @NotNull
    private OrderType orderType;

    @NotBlank
    private String orderSlug;

    public StoreOrder from(long clientId, CartResponse cartResponse) {
        return StoreOrder.builder()
                .clientId(clientId)
                .storeId((this.storeId))
                .cartId(cartResponse == null ? this.cartId : cartResponse.getId())
                .userId(this.userId)
                .orderType(this.orderType)
                .orderSlug(this.orderSlug)
                .build();
    }

    public void validateStoreOrderRequest() {
        if ((this.cartId == null || this.cartId == 0L) && this.cartRequest == null) {
            throw new ProcessException("Order creation", "Both cartId and cartRequest cannot be null");
        }
    }

}
