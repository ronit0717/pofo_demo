package com.vinava.pofo.dto.response;

import com.vinava.pofo.enumeration.StockMovementReferenceType;
import com.vinava.pofo.enumeration.StockMovementType;
import com.vinava.pofo.model.StockMovement;
import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.*;

@Data
@Builder
@Slf4j
public class StockMovementResponse {

    private long id;
    private long clientId;
    private long storeId;
    private long stockId;
    private BigDecimal quantity;
    private StockMovementType stockMovementType;
    private StockMovementReferenceType stockMovementReferenceType;
    private String referenceId;
    private String comment;
    private Date createdOn;
    private Date updatedOn;

    public static StockMovementResponse from(StockMovement stockMovement) {
        return StockMovementResponse.builder()
                .id(stockMovement.getId())
                .clientId(stockMovement.getClientId())
                .storeId(stockMovement.getStoreId())
                .stockId(stockMovement.getStockId())
                .quantity(stockMovement.getQuantity())
                .stockMovementType(stockMovement.getStockMovementType())
                .stockMovementReferenceType(stockMovement.getStockMovementReferenceType())
                .referenceId(stockMovement.getReferenceId())
                .comment(stockMovement.getComment())
                .createdOn(stockMovement.getCreatedOn())
                .updatedOn(stockMovement.getUpdatedOn())
                .build();
    }

    private static List<StockMovementResponse> from(List<StockMovement> stockMovements) {
        List<StockMovementResponse> stockMovementResponses = new LinkedList<>();
        for (StockMovement stockMovement: stockMovements) {
            stockMovementResponses.add(from(stockMovement));
        }
        return stockMovementResponses;
    }

    public static ResponseEntity<List<StockMovementResponse>> getResponseEntityFrom(List<StockMovement> stockMovements) {
        try {
            List<StockMovementResponse> productResponses = from(stockMovements);
            HttpHeaders headers = new HttpHeaders();
            headers.add("X-Total-Count", String.valueOf(productResponses.size()));
            headers.add("Access-Control-Expose-Headers", "X-Total-Count");

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(productResponses);
        } catch (Exception e) {
            log.error("In exception block of getResponseEntityFrom for list of stock movements: {}", stockMovements, e);
            return ResponseEntity.badRequest().build();
        }
    }

}
