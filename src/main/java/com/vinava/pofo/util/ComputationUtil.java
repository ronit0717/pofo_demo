package com.vinava.pofo.util;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class ComputationUtil {

    public static final BigDecimal ONE_HUNDRED = new BigDecimal(100);

    public static BigDecimal getDiscountedPrice(BigDecimal originalPrice, BigDecimal discountPercentage) {
        BigDecimal discount = (originalPrice.multiply(discountPercentage)).divide(ONE_HUNDRED, RoundingMode.CEILING);
        return originalPrice.subtract(discount);
    }

    public static BigDecimal getFinalAmount(BigDecimal taxablePrice, BigDecimal taxPercentage) {
        BigDecimal tax = taxPercentage == null ? BigDecimal.ZERO
                : (taxablePrice.multiply(taxPercentage)).divide(ONE_HUNDRED, RoundingMode.CEILING);
        return taxablePrice.add(tax);
    }

    public static boolean isValidPercentage(BigDecimal percentage) {
        if (percentage == null) {
            return true;
        }
        int result = percentage.compareTo(ONE_HUNDRED);
        return result <= 0;
    }

}
