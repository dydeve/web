package dydeve.db.datasource.annotation;

import java.lang.annotation.*;

/**
 * Created by dy on 2017/8/20.
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface DataSourceGroup {

    String dataSourceGroup = "";

    String value() default dataSourceGroup;

}
