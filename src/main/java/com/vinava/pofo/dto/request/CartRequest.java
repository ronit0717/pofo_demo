package com.vinava.pofo.dto.request;

import com.vinava.pofo.model.Cart;
import com.vinava.pofo.model.embed.CartEntity;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@Builder
public class CartRequest {

    private Long userId;

    @NotNull
    private List<CartEntity> cartEntities;

    public Cart from(long clientId) {
        return Cart.builder()
                .clientId(clientId)
                .userId(this.userId)
                .cartEntities(this.cartEntities)
                .build();
    }

}
