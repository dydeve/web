package dydeve.monitor.util;

import javafx.util.Pair;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by dy on 2017/8/2.
 */
public abstract class PairSnapShat<T> implements SnapShot<String, Object, Pair<String, Object>, T> {

    private List<Pair<String, Object>> pairs = new LinkedList<>();

    @Override
    public T transfer() {
        return string(pairs);
    }

    public abstract T string(List<Pair<String, Object>> pairs);

    @Override
    public void set(String key, Object value) {
        pairs.add(new Pair<>(key, value));
    }

    @Override
    public Object get(String key) {
        for (Pair<String, Object> pair : pairs) {
            if (pair.getKey() == key) {//key 是接口中的常量  可以用 ==
                return pair.getValue();
            }
        }
        return null;
    }

    @Override
    public List<String> keys() {
        return pairs.stream().map(p -> p.getKey()).collect(Collectors.toList());
    }

    @Override
    public Iterator<Pair<String, Object>> iterator() {
        return pairs.iterator();
    }
}
