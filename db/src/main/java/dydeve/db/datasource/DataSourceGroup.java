package dydeve.db.datasource;

import javax.sql.DataSource;
import java.util.Collection;

/**
 * Created by dy on 2017/8/20.
 */
public interface DataSourceGroup {

    String getGroupId();

    DataSource getMaster();

    Collection<DataSource> getSlaves();

}
