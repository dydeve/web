package com.dydeve.web.handler.annotation;

/**
 * convert ?{...} or ?d={...}&c={...} to object
 * Created by dy on 2017/7/21.
 */
public @interface RequestJson {

    String value() default "";

    boolean required() default true;

}
