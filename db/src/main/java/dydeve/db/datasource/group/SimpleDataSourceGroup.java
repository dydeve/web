package dydeve.db.datasource.group;

import dydeve.db.datasource.IdedDataSource;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by dy on 2017/8/20.
 */
public class SimpleDataSourceGroup implements DataSourceGroup {

    private final String groupId;

    private final IdedDataSource master;

    private final Collection<IdedDataSource> slaves;

    private final Map<String, IdedDataSource> slavesMap = new HashMap<>();

    protected SimpleDataSourceGroup(String groupId, IdedDataSource master, Collection<IdedDataSource> slaves) {
        this.groupId = groupId;
        this.master = master;
        this.slaves = slaves;
        if (slaves != null && !slaves.isEmpty()) {
            for (IdedDataSource idedDataSource : slaves) {
                slavesMap.put(idedDataSource.getDataSourceId(), idedDataSource);
            }
        }
    }

    @Override
    public String getGroupId() {
        return groupId;
    }

    @Override
    public IdedDataSource getMaster() {
        return master;
    }

    @Override
    public Collection<IdedDataSource> getSlaves() {
        return slaves;
    }

    @Override
    public IdedDataSource getSlave(String dataSourceId) {
        return slavesMap.get(dataSourceId);
    }

    @Override
    public IdedDataSource getSpecifiedDataSource(String dataSourceId) {
        if (dataSourceId.equals(master.getDataSourceId())) {
            return master;
        }
        return getSlave(dataSourceId);
    }

}
