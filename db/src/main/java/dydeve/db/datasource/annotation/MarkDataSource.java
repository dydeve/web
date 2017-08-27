package dydeve.db.datasource.annotation;

import java.lang.annotation.*;

/**
 * mark dataSource
 *
 * usually, we read from slaves and write to master.
 * but if case of read operation in a transaction, we read from the master db.
 * Created by dy on 2017/8/26.
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface MarkDataSource {

    boolean master() default false;

    String dataSourceId() default "";

}
