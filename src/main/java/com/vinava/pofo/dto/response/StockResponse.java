package com.vinava.pofo.dto.response;

import com.vinava.pofo.dto.request.StockRequest;
import com.vinava.pofo.model.Stock;
import com.vinava.pofo.service.ProductService;
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
    private ProductResponse product;
    private BigDecimal price;
    private BigDecimal discountPercentage;
    private BigDecimal quantity;
    private BigDecimal refillLevel;
    private boolean forSale;
    private Date createdOn;
    private Date updatedOn;

    public static StockResponse from(Stock stock, ProductService productService) {
        ProductResponse productResponse = productService.getProductById(stock.getProductId(), stock.getClientId());
        return StockResponse.builder()
                .id(stock.getId())
                .clientId(stock.getClientId())
                .storeId(stock.getStoreId())
                .price(stock.getPrice())
                .product(productResponse)
                .discountPercentage(stock.getDiscountPercentage())
                .quantity(stock.getQuantity())
                .refillLevel(stock.getRefillLevel())
                .forSale(stock.isForSale())
                .createdOn(stock.getCreatedOn())
                .updatedOn(stock.getUpdatedOn())
                .build();
    }

    private static List<StockResponse> from(List<Stock> products, ProductService productService) {
        List<StockResponse> productResponses = new LinkedList<>();
        for (Stock product: products) {
            productResponses.add(from(product, productService));
        }
        return productResponses;
    }

    public static ResponseEntity<List<StockResponse>> getResponseEntityFrom(List<Stock> stocks, ProductService productService) {
        try {
            List<StockResponse> stockResponses = from(stocks, productService);
            HttpHeaders headers = new HttpHeaders();
            headers.add("X-Total-Count", String.valueOf(stockResponses.size()));
            headers.add("Access-Control-Expose-Headers", "X-Total-Count");

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(stockResponses);
        } catch (Exception e) {
            log.error("In exception block of getResponseEntityFrom for list of stocks: {}", stocks, e);
            return ResponseEntity.badRequest().build();
        }
    }

    public StockRequest from(BigDecimal newQuantity) {
        return StockRequest.builder()
                .storeId(this.storeId)
                .refillLevel(this.refillLevel)
                .quantity(newQuantity)
                .productId(this.product.getId())
                .discountPercentage(this.discountPercentage)
                .forSale(this.forSale)
                .price(this.price)
                .build();
    }

}
