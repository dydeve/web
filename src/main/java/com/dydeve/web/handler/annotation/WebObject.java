package com.dydeve.web.handler.annotation;

import java.lang.annotation.*;

/**
 * indicate that need convert "?a=...&b=..." to object
 * Created by dy on 2017/7/20.
 */
@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface WebObject {
}
