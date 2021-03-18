package com.vinava.pofo.dto.request;

import com.vinava.pofo.model.Location;
import com.vinava.pofo.model.embed.ProductAttribute;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Set;

@Data
@Builder
public class LocationRequest {

    @NotBlank
    @Size(max = 30)
    private String name;

    @NotNull
    @Size(max = 6)
    private String pincode;

    private String description;

    private Set<ProductAttribute> productAttributes;

    public Location from(long clientId) {
        return Location.builder()
                .clientId(clientId)
                .name((this.name))
                .description(this.description)
                .pincode(this.pincode)
                .build();
    }

}
