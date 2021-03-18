package com.vinava.pofo.dto.request;

import com.vinava.pofo.model.Brand;
import com.vinava.pofo.model.embed.ProductAttribute;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Set;

@Data
@Builder
public class BrandRequest {

    @NotBlank
    @Size(max = 30)
    private String name;

    private String brandLogo;

    private String brandLink;

    private String description;

    private Set<ProductAttribute> productAttributes;

    public Brand from(long clientId) {
        return Brand.builder()
                .clientId(clientId)
                .name((this.name))
                .description(this.description)
                .brandLogo(this.brandLogo)
                .brandLink(this.brandLink)
                .build();
    }

}
