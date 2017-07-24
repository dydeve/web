package dydeve.site.web.handler.annotation;

import com.dydeve.web.vo.ApiResponse;

import java.lang.annotation.*;

/**
 * convert the response or response.data to json
 * Created by dy on 2017/7/21.
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ResponseJson {

    /**
     * @see ApiResponse#data
     */
    int data = 1;
    /**
     * @see ApiResponse
     */
    int response = 2;

    int value() default data;

}
