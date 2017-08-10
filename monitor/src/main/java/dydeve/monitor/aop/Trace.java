package dydeve.monitor.aop;

import java.lang.annotation.*;

/**
 * Created by dy on 2017/8/2.
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Trace {

    /**
     * description of traced method
     *
     * @return
     */
    String description();

    /**
     * record params
     *
     * @return
     */
    boolean recordParams() default false;

    /**
     * record result
     */
    boolean recordResult() default false;



}
