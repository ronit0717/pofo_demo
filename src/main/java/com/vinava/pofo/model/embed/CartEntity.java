package com.vinava.pofo.model.embed;

import com.vinava.pofo.enumeration.CategoryType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.math.BigDecimal;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class CartEntity {

    @Column(nullable = false)
    private long stockId;

    private BigDecimal unitPrice;

    private BigDecimal discountPercentage;

    private BigDecimal quantity;

    private BigDecimal gstPercentage;

    @Enumerated(value = EnumType.STRING)
    private CategoryType cartEntityType;

}
