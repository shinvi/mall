package com.shinvi.mall.util;


import com.google.common.collect.Sets;

import java.util.Objects;
import java.util.Set;

/**
 * @author 邱长海
 */
public class ObjectUtils {

    public static <E> E with(E e, Function<E> fun) {
        fun.invoke(e);
        return e;
    }

    public static <E extends AutoCloseable> void autoClose(E e, Function<E> fun) {
        fun.invoke(e);
    }

    public static <E extends AutoCloseable, T> T autoClose(E e, Function2<E, T> fun) {
        return fun.invoke(e);
    }

    public static boolean in(Object source, Object... other) {
        if (other == null) {
            return false;
        }
        Set<Object> others = Sets.newHashSet(other);
        return others.contains(source);
    }

}
