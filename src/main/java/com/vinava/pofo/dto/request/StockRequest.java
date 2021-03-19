package com.vinava.pofo.dto.request;

import com.vinava.pofo.model.Stock;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;

@Data
@Builder
public class StockRequest {

    @NotBlank
    private Long productId;

    @NotBlank
    private Long storeId;

    private Integer quantity;

    private Integer refillLevel;

    private BigDecimal price;

    private BigDecimal discountPercentage;

    private boolean forSale;

    public Stock from(long clientId) {
        return Stock.builder()
                .clientId(clientId)
                .productId(this.productId)
                .storeId(this.storeId)
                .quantity(this.quantity)
                .refillLevel(this.refillLevel)
                .price(this.price)
                .discountPercentage(this.discountPercentage)
                .forSale(this.forSale)
                .build();
    }

}
