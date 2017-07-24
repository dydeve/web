package dydeve.web.handler.annotation;

import com.dydeve.util.callback.Callback;
import com.dydeve.util.callback.FailureCallback;

import java.lang.annotation.*;

/**
 * indicate that need convert "?a=...&b=..." to object.
 * the target class must has a public constructor without parameter!
 * Created by dy on 2017/7/20.
 */
@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface WebObject {

    boolean required() default true;

}
