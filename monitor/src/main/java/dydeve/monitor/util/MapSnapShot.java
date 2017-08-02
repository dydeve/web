package dydeve.monitor.util;

import java.util.*;

/**
 * snapShop made of map
 * Created by dy on 2017/8/2.
 */
public abstract class MapSnapShot<T> implements SnapShot<String, Object, Map.Entry<String, Object>, T>{

    Map<String, Object> kvs = new LinkedHashMap<>();

    @Override
    public T transfer() {
        return transfer(kvs);
    }

    protected abstract T transfer(Map<String, Object> kvs);

    @Override
    public void set(String key, Object value) {
        kvs.put(key, value);
    }

    @Override
    public Object get(String key) {
        return kvs.get(key);
    }

    @Override
    public Set<String> keys() {
        return kvs.keySet();
    }

    @Override
    public Iterator<Map.Entry<String, Object>> iterator() {
        return kvs.entrySet().iterator();
    }
}
