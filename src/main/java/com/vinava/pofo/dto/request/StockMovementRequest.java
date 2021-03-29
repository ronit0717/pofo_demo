package com.vinava.pofo.dto.request;

import com.vinava.pofo.enumeration.StockMovementReferenceType;
import com.vinava.pofo.enumeration.StockMovementType;
import com.vinava.pofo.model.StockMovement;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;

@Data
@Builder
public class StockMovementRequest {

    @NotNull
    private Long stockId;

    @NotNull
    private Long storeId;

    @NotNull
    private BigDecimal quantity;

    @NotNull
    private StockMovementType stockMovementType;

    @NotNull
    private StockMovementReferenceType stockMovementReferenceType;

    @Size(max = 50)
    private String referenceId;

    @Size(max = 1024)
    private String comment;

    public StockMovement from(long clientId) {
        return StockMovement.builder()
                .clientId(clientId)
                .stockId(this.stockId)
                .storeId(this.storeId)
                .quantity(this.quantity)
                .stockMovementType(this.stockMovementType)
                .stockMovementReferenceType(this.stockMovementReferenceType)
                .referenceId(this.referenceId)
                .comment(this.comment)
                .build();
    }

}
