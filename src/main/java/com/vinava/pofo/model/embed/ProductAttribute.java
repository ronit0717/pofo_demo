package com.vinava.pofo.model.embed;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ProductAttribute {

    @NotBlank
    @Column(length = 200, nullable = false)
    private String attributeKey;

    @NotBlank
    @Column(length = 500, nullable = false)
    private String attributeValue;

    private Integer attributeOrder;

}
