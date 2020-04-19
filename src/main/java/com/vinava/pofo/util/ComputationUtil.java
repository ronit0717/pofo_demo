package com.vinava.pofo.util;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class ComputationUtil {

    public static final BigDecimal ONE_HUNDRED = new BigDecimal(100);

    public static BigDecimal getDiscountedPrice(BigDecimal originalPrice, BigDecimal discountPercentage) {
        BigDecimal discount = (originalPrice.multiply(discountPercentage)).divide(ONE_HUNDRED, RoundingMode.CEILING);
        return originalPrice.subtract(discount);
    }

}
