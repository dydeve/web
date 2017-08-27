package dydeve.db.datasource;

import javax.sql.DataSource;

/**
 * DATASOURCE WITH id.
 * individual for each dataSourceGroup
 * Created by dy on 2017/8/26.
 */
public interface IdedDataSource extends DataSource {

    String getDataSourceId();

}
