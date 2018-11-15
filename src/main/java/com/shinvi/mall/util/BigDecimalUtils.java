package com.shinvi.mall.util;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * @author 邱长海
 */
public class BigDecimalUtils {

    public static BigDecimal add(Number a, Number b) {
        return new BigDecimal(a.toString()).add(new BigDecimal(b.toString()));
    }

    public static BigDecimal sub(Number a, Number b) {
        return new BigDecimal(a.toString()).subtract(new BigDecimal(b.toString()));
    }

    public static BigDecimal multiply(Number a, Number b) {
        return new BigDecimal(a.toString()).multiply(new BigDecimal(b.toString()));
    }

    public static BigDecimal divide(Number a, Number b) {
        return new BigDecimal(a.toString()).divide(new BigDecimal(b.toString()), 2, RoundingMode.UP);
    }

    public static BigDecimal divide(Number a, Number b, int scale) {
        return new BigDecimal(a.toString()).divide(new BigDecimal(b.toString()), scale, RoundingMode.UP);
    }
}
