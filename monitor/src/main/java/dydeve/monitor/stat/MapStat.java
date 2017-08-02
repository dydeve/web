package dydeve.monitor.stat;

import com.alibaba.fastjson.JSON;
import dydeve.monitor.util.MapSnapShot;
import dydeve.monitor.util.SnapShot;

import java.util.*;

/**
 * Created by dy on 2017/8/1.
 */
public class MapStat implements IStat<Map.Entry<String, Object>, String> {

    SnapShot<String, Object, Map.Entry<String, Object>, String> snapShot = new MapSnapShot<String>() {
        @Override
        protected String transfer(Map<String, Object> kvs) {
            return JSON.toJSONString(kvs);
        }
    };

    @Override
    public SnapShot<String, Object, Map.Entry<String, Object>, String> snapShot() {
        return snapShot;
    }

    @Override
    public Collection<String> attributeNames() {
        return snapShot.keys();
    }

    @Override
    public Object getAttributeValue(String attributeName) {
        return snapShot.get(attributeName);
    }

    @Override
    public void setAttribute(String attributeName, Object attributeValue) {
        snapShot.set(attributeName, attributeValue);
    }


}
