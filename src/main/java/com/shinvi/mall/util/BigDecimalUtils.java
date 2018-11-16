package com.shinvi.mall.util;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * @author 邱长海
 */
public class BigDecimalUtils {

    public static BigDecimal add(Number a, Number b) {
        return (a instanceof BigDecimal ? ((BigDecimal) a) : new BigDecimal(a.toString()))
                .add((b instanceof BigDecimal ? ((BigDecimal) b) : new BigDecimal(b.toString())));
    }

    public static BigDecimal add(Number... numbers) {
        BigDecimal result = BigDecimal.ZERO;
        for (Number number : numbers) {
            result = add(result, number);
        }
        return result;
    }

    public static BigDecimal sub(Number a, Number b) {
        return (a instanceof BigDecimal ? ((BigDecimal) a) : new BigDecimal(a.toString()))
                .subtract((b instanceof BigDecimal ? ((BigDecimal) b) : new BigDecimal(b.toString())));
    }

    public static BigDecimal multiply(Number a, Number b) {
        return (a instanceof BigDecimal ? ((BigDecimal) a) : new BigDecimal(a.toString()))
                .multiply((b instanceof BigDecimal ? ((BigDecimal) b) : new BigDecimal(b.toString())));
    }

    public static BigDecimal divide(Number a, Number b) {
        return (a instanceof BigDecimal ? ((BigDecimal) a) : new BigDecimal(a.toString()))
                .divide((b instanceof BigDecimal ? ((BigDecimal) b) : new BigDecimal(b.toString())), 2, RoundingMode.UP);
    }

    public static BigDecimal divide(Number a, Number b, int scale) {
        return (a instanceof BigDecimal ? ((BigDecimal) a) : new BigDecimal(a.toString()))
                .divide((b instanceof BigDecimal ? ((BigDecimal) b) : new BigDecimal(b.toString())), scale, RoundingMode.UP);
    }
}
