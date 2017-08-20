package dydeve.db.datasource;

import com.google.common.collect.Maps;

import java.util.Map;

/**
 * Created by dy on 2017/8/20.
 */
public class DataSourceGroups {

    /**
     * 顺序很重要
     */
    private static final Map<String, DataSourceGroup> dataSourceGroups = Maps.newLinkedHashMap();

    public void setDataSourceGroup(String groupId, DataSourceGroup dataSourceGroup) {
        dataSourceGroups.put(groupId, dataSourceGroup);
    }

    public DataSourceGroup getDataSourceGroup(String groupId) {
        return dataSourceGroups.get(groupId);
    }

}
