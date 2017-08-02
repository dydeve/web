package dydeve.monitor.common;

import java.lang.annotation.*;

/**
 * Created by yuduy on 2017/8/2.
 */
@Target({ElementType.METHOD, ElementType.TYPE, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface NotThreadSafe {
}
