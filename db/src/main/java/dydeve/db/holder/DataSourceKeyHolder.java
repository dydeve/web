package dydeve.db.holder;

/**
 * Created by dy on 2017/8/20.
 */
public class DataSourceKeyHolder {

    private static final ThreadLocal<String> groupIdThreadlLocal = new ThreadLocal<>();
    private static final ThreadLocal<String> dataSourceThreadLocal = new ThreadLocal<>();

    public static String getGroupId() {
        return groupIdThreadlLocal.get();
    }

    public static void setGroupId(String groupId) {
        DataSourceKeyHolder.groupIdThreadlLocal.set(groupId);
    }

    public static String getDataSourceId() {
        return dataSourceThreadLocal.get();
    }

    public static void  setDataSourceId(String dataSourceId) {
        DataSourceKeyHolder.dataSourceThreadLocal.set(dataSourceId);
    }

    public static void remove() {
        removeGroupId();
        removeDataSourceId();
    }

    public static void removeGroupId() {
        groupIdThreadlLocal.remove();
    }

    public static void removeDataSourceId() {
        dataSourceThreadLocal.remove();
    }

}
