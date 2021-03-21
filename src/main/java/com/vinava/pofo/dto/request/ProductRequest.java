package com.vinava.pofo.dto.request;

import com.vinava.pofo.enumeration.ProductPricingType;
import com.vinava.pofo.enumeration.QuantityType;
import com.vinava.pofo.model.Product;
import com.vinava.pofo.model.embed.ProductAttribute;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.Set;

@Data
@Builder
public class ProductRequest {

    private Long categoryId;

    private Long brandId;

    @NotBlank
    @Size(max = 200)
    private String name;

    @NotNull
    private ProductPricingType productPricingType;

    @NotNull
    private QuantityType quantityType;

    private BigDecimal price;

    private BigDecimal discountPercentage;

    private Set<String> productImages;

    private String description;

    private Set<ProductAttribute> productAttributes;

    public Product from(long clientId) {
        return Product.builder()
                .clientId(clientId)
                .categoryId(this.getCategoryId())
                .brandId(this.getBrandId())
                .quantityType(this.getQuantityType())
                .name((this.name))
                .productPricingType(this.productPricingType)
                .price(this.price)
                .discountPercentage(this.discountPercentage)
                .productImages(this.productImages)
                .description(this.description)
                .productAttributes(this.productAttributes)
                .build();
    }

    public Long getCategoryId() {
        if (this.categoryId != null && this.categoryId != 0L) {
            return this.categoryId;
        }
        return null;
    }

    public Long getBrandId() {
        if (this.brandId != null && this.brandId != 0L) {
            return this.brandId;
        }
        return null;
    }

}
