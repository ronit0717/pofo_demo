package com.vinava.pofo.dto.request;

import com.vinava.pofo.model.Store;
import com.vinava.pofo.model.embed.ProductAttribute;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Set;

@Data
@Builder
public class StoreRequest {

    @NotBlank
    @Size(max = 30)
    private String name;

    @NotNull
    private Long locationId;

    private String description;

    private String address;

    @Size(max = 15, message = "Invalid GSTIN")
    private String gstin;

    private Set<ProductAttribute> productAttributes;

    public Store from(long clientId) {
        return Store.builder()
                .clientId(clientId)
                .name((this.name))
                .description(this.description)
                .locationId(this.locationId)
                .address(this.address)
                .gstin(this.gstin)
                .build();
    }

}
