package com.vinava.pofo.dto.response;

import com.vinava.pofo.enumeration.ProductPricingType;
import com.vinava.pofo.model.Product;
import com.vinava.pofo.model.embed.ProductAttribute;
import com.vinava.pofo.util.ComputationUtil;
import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Data
@Builder
@Slf4j
public class ProductResponse {

    private long id;
    private long clientId;
    private String name;
    private Long productCategoryId;
    private ProductPricingType productPricingType;
    private BigDecimal price;
    private BigDecimal discountPercentage;
    private BigDecimal sellingPrice;
    private Set<Long> productImageIds;
    private Set<ProductAttribute> productAttributes;
    private String description;
    private Date createdOn;
    private Date updatedOn;

    public static ProductResponse from(Product product) {
        return ProductResponse.builder()
                .id(product.getId())
                .clientId(product.getClientId())
                .name(product.getName())
                .productCategoryId(product.getProductCategoryId())
                .price(product.getPrice())
                .discountPercentage(product.getDiscountPercentage())
                .sellingPrice(ComputationUtil.getDiscountedPrice(product.getPrice(), product.getDiscountPercentage()))
                .productPricingType(product.getProductPricingType())
                .productImageIds(product.getProductImageIds())
                .productAttributes(product.getProductAttributes())
                .description(product.getDescription())
                .createdOn(product.getCreatedOn())
                .updatedOn(product.getUpdatedOn())
                .build();
    }

    private static List<ProductResponse> from(List<Product> products) {
        List<ProductResponse> productResponses = new ArrayList<>();
        for (Product product: products) {
            productResponses.add(from(product));
        }
        return productResponses;
    }

    public static ResponseEntity<List<ProductResponse>> getResponseEntityFrom(List<Product> products) {
        try {
            List<ProductResponse> clientResponses = from(products);
            HttpHeaders headers = new HttpHeaders();
            headers.add("X-Total-Count", String.valueOf(clientResponses.size()));
            headers.add("Access-Control-Expose-Headers", "X-Total-Count");

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(clientResponses);
        } catch (Exception e) {
            log.error("In exception block of getResponseEntityFrom for list of products: {}", products, e);
            return ResponseEntity.badRequest().build();
        }
    }

}
