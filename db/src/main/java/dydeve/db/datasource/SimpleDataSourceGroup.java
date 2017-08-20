package dydeve.db.datasource;

import javax.sql.DataSource;
import java.util.Collection;

/**
 * Created by dy on 2017/8/20.
 */
public class SimpleDataSourceGroup implements DataSourceGroup {

    private String groupId;

    private DataSource master;

    private Collection<DataSource> slaves;

    @Override
    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    @Override
    public DataSource getMaster() {
        return master;
    }

    public void setMaster(DataSource master) {
        this.master = master;
    }

    @Override
    public Collection<DataSource> getSlaves() {
        return slaves;
    }

    public void setSlaves(Collection<DataSource> slaves) {
        this.slaves = slaves;
    }
}
