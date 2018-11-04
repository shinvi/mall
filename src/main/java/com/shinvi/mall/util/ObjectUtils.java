package com.shinvi.mall.util;


/**
 * @author 邱长海
 */
public class ObjectUtils {

    public static <E> E with(E e, Function<E> fun) {
        try {
            fun.invoke(e);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return e;
    }

    public static <E extends AutoCloseable> void autoClose(E e, Function<E> fun) {
        try (e) {
            fun.invoke(e);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public static <E extends AutoCloseable, T> T autoClose(E e, Function2<E, T> fun) {
        try (e) {
            return fun.invoke(e);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return null;
    }
}
