package com.vinava.pofo.dto.response;

import com.vinava.pofo.model.Stock;
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
public class StockResponse {

    private long id;
    private long clientId;
    private long storeId;
    private long productId;
    private BigDecimal price;
    private BigDecimal discountPercentage;
    private Integer quantity;
    private Integer refillLevel;
    private boolean forSale;
    private Date createdOn;
    private Date updatedOn;

    public static StockResponse from(Stock stock) {
        return StockResponse.builder()
                .id(stock.getId())
                .clientId(stock.getClientId())
                .productId(stock.getProductId())
                .storeId(stock.getStoreId())
                .price(stock.getPrice())
                .discountPercentage(stock.getDiscountPercentage())
                .quantity(stock.getQuantity())
                .refillLevel(stock.getRefillLevel())
                .createdOn(stock.getCreatedOn())
                .updatedOn(stock.getUpdatedOn())
                .build();
    }

    private static List<StockResponse> from(List<Stock> products) {
        List<StockResponse> productResponses = new LinkedList<>();
        for (Stock product: products) {
            productResponses.add(from(product));
        }
        return productResponses;
    }

    public static ResponseEntity<List<StockResponse>> getResponseEntityFrom(List<Stock> products) {
        try {
            List<StockResponse> productResponses = from(products);
            HttpHeaders headers = new HttpHeaders();
            headers.add("X-Total-Count", String.valueOf(productResponses.size()));
            headers.add("Access-Control-Expose-Headers", "X-Total-Count");

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(productResponses);
        } catch (Exception e) {
            log.error("In exception block of getResponseEntityFrom for list of products: {}", products, e);
            return ResponseEntity.badRequest().build();
        }
    }

}
