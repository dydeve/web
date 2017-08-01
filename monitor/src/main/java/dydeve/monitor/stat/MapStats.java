package dydeve.monitor.stat;

import java.util.*;

/**
 * Created by dy on 2017/8/1.
 */
public class MapStats implements IStats<Map.Entry<String, Object>> {

    private Map<String, Object> kvs = new LinkedHashMap<>();

    @Override
    public Set<String> keys() {
        return kvs.keySet();
    }

    @Override
    public Object get(String key) {
        return kvs.get(key);
    }

    @Override
    public void set(String key, Object value) {
        kvs.put(key, value);
    }

    @Override
    public Iterator<Map.Entry<String, Object>> iterator() {
        return kvs.entrySet().iterator();
    }
}
