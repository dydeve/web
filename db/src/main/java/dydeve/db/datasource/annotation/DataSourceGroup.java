package dydeve.db.datasource.annotation;

/**
 * Created by dy on 2017/8/20.
 */
public @interface DataSourceGroup {

    String defaultDataSourceGroup = "";

    String value() default defaultDataSourceGroup;

}
