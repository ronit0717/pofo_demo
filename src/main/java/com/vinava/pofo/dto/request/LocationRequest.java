package com.vinava.pofo.dto.request;

import com.vinava.pofo.model.Location;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@Builder
public class LocationRequest {

    @NotBlank
    @Size(max = 30)
    private String name;

    @NotNull
    @Size(min = 6, max = 6, message = "Invalid Pincode")
    private String pincode;

    private String description;

    public Location from(long clientId) {
        return Location.builder()
                .clientId(clientId)
                .name((this.name))
                .description(this.description)
                .pincode(this.pincode)
                .build();
    }

}
