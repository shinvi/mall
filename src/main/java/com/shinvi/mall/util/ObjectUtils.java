package com.shinvi.mall.util;


import java.util.Objects;

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
        for (Object o : other) {
            if (Objects.equals(source, o)) {
                return true;
            }
        }
        return false;
    }

}
