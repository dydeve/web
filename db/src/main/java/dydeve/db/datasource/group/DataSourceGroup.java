package dydeve.db.datasource.group;

import dydeve.db.datasource.IdedDataSource;

import java.util.Collection;

/**
 * Created by dy on 2017/8/20.
 */
public interface DataSourceGroup {

    String getGroupId();

    IdedDataSource getMaster();

    Collection<IdedDataSource> getSlaves();

    IdedDataSource getSlave(String dataSourceId);

    IdedDataSource getSpecifiedDataSource(String dataSourceId);

}
