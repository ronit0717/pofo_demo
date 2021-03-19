package com.vinava.pofo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Address {

    @NotBlank
    @Size(max = 100)
    @Column(length = 100, nullable = false)
    private String addressLine1;

    @Size(max = 100)
    @Column(length = 100)
    private String addressLine2;

    @NotBlank
    @Size(max = 100)
    @Column(length = 100, nullable = false)
    private String city;

    @NotBlank
    @Size(max = 60)
    @Column(length = 60, nullable = false)
    private String state;

    @NotBlank
    @Size(max = 60)
    @Column(length = 60, nullable = false)
    private String country;

    @NotBlank
    @Size(min = 6, max = 6, message = "Invalid Pincode")
    @Column(length = 6, nullable = false)
    private String pincode;

}
