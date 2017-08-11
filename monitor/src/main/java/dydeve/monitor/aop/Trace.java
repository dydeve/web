package dydeve.monitor.aop;

import java.lang.annotation.*;

/**
 * Created by dy on 2017/8/2.
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Trace {

    String NULL = "";

    /**
     * value of traced method
     *
     * @return
     */
    String value() default NULL;

    /**
     * record params
     *
     * @return
     */
    boolean recordParams() default true;

    /**
     * record result
     */
    boolean recordResult() default true;



}
