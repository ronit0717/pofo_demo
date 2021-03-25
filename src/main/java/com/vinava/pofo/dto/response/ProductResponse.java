package com.vinava.pofo.dto.response;

import com.vinava.pofo.enumeration.ProductPricingType;
import com.vinava.pofo.enumeration.QuantityType;
import com.vinava.pofo.model.Product;
import com.vinava.pofo.model.embed.ProductAttribute;
import com.vinava.pofo.service.BrandService;
import com.vinava.pofo.service.CategoryService;
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
public class ProductResponse {

    private long id;
    private long clientId;
    private String name;
    private CategoryResponse category;
    private BrandResponse brand;
    private ProductPricingType productPricingType;
    private BigDecimal price;
    private BigDecimal discountPercentage;
    private Set<String> productImages;
    private QuantityType quantityType;
    private Set<ProductAttribute> productAttributes;
    private String description;
    private Date createdOn;
    private Date updatedOn;

    public static ProductResponse from(Product product, CategoryService categoryService, BrandService brandService) {
        CategoryResponse categoryResponse = (product.getCategoryId() == null || product.getCategoryId() == 0L) ? null
                : categoryService.getById(product.getCategoryId(), product.getClientId());
        BrandResponse brandResponse = (product.getBrandId() == null || product.getBrandId() == 0L) ? null
                : brandService.getBrandById(product.getBrandId(), product.getClientId());
        return ProductResponse.builder()
                .id(product.getId())
                .clientId(product.getClientId())
                .name(product.getName())
                .category(categoryResponse)
                .brand(brandResponse)
                .price(product.getPrice())
                .quantityType(product.getQuantityType())
                .discountPercentage(product.getDiscountPercentage())
                .productPricingType(product.getProductPricingType())
                .productImages(product.getProductImages())
                .productAttributes(product.getProductAttributes())
                .description(product.getDescription())
                .createdOn(product.getCreatedOn())
                .updatedOn(product.getUpdatedOn())
                .build();
    }

    private static List<ProductResponse> from(List<Product> products, CategoryService categoryService, BrandService brandService) {
        List<ProductResponse> productResponses = new LinkedList<>();
        for (Product product: products) {
            productResponses.add(from(product, categoryService, brandService));
        }
        return productResponses;
    }

    public static ResponseEntity<List<ProductResponse>> getResponseEntityFrom
            (List<Product> products, CategoryService categoryService, BrandService brandService) {
        try {
            List<ProductResponse> productResponses = from(products, categoryService, brandService);
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
