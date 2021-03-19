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

    private Long productCategoryId;

    @NotBlank
    @Size(max = 200)
    private String name;

    @NotNull
    private ProductPricingType productPricingType;

    @NotNull
    private QuantityType quantityType;

    @NotNull
    private BigDecimal price;

    @NotNull
    private BigDecimal discountPercentage;

    private Set<String> productImages;

    private String description;

    private Set<ProductAttribute> productAttributes;

    public Product from(long clientId) {
        return Product.builder()
                .clientId(clientId)
                .productCategoryId(this.productCategoryId)
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

}
