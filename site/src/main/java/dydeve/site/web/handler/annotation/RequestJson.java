package dydeve.site.web.handler.annotation;

import java.lang.annotation.*;

/**
 * convert ?{...} or ?d={...}&c={...} to object
 * Created by dy on 2017/7/21.
 */
@Target({ElementType.METHOD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequestJson {

    String value() default "";

    boolean required() default true;

}
