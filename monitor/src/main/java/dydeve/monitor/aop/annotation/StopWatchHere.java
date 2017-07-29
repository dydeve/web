package dydeve.monitor.aop.annotation;

import java.lang.annotation.*;

/**
 * Created by yuduy on 2017/7/29.
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface StopWatchHere {


    /**
     * when catch exception, throw it
     * @return
     */
    boolean throwException() default true;

    /**
     * description of monitored object
     * @return
     */
    String description();

    /**
     * record params
     * @return
     */
    boolean recordParams() default false;

    /**
     * record result
     */
    boolean recordResult() default false;


}
