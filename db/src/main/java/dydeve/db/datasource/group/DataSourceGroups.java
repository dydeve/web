package dydeve.db.datasource.group;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Created by dy on 2017/8/20.
 */
public class DataSourceGroups {

    /**
     * 顺序很重要
     */
    private static final ConcurrentMap<String, DataSourceGroup> dataSourceGroups = new ConcurrentHashMap<>();

    public void setDataSourceGroup(String groupId, DataSourceGroup dataSourceGroup) {
        dataSourceGroups.put(groupId, dataSourceGroup);
    }

    public DataSourceGroup getDataSourceGroup(String groupId) {
        return dataSourceGroups.get(groupId);
    }

}
