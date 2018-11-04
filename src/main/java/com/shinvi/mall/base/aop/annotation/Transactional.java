package com.shinvi.mall.base.aop.annotation;

import java.lang.annotation.*;

/**
 * @author 邱长海
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
@org.springframework.transaction.annotation.Transactional(rollbackFor = Exception.class)
public @interface Transactional {
}
