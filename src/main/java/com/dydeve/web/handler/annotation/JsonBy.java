package com.dydeve.web.handler.annotation;

import java.lang.annotation.*;

/**
 * serialize and deserialize by;
 * Created by dy on 2017/7/22.
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface JsonBy {

    String WEB_JSONBY = "web.jsonBy";

    int FASTJSON = 0;
    int GSON = 1;
    int JACKSON = 2;

    int value() default FASTJSON;


}
