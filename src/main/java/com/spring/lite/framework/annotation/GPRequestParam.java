package com.spring.lite.framework.annotation;

import java.lang.annotation.*;

/**
 * @author wangtianqi20
 * @Description
 * @date 2021-05-25
 */
@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface GPRequestParam {

    String value() default "";
}
