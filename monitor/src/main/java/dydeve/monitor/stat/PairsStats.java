package dydeve.monitor.stat;

import javafx.util.Pair;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by dy on 2017/8/1.
 */
public class PairsStats implements IStats<Pair<String, Object>> {

    private List<Pair<String, Object>> pairs = new LinkedList<>();

    @Override
    public List<String> keys() {
        return pairs.stream().map(p -> p.getKey()).collect(Collectors.toList());
    }

    @Override
    public Object get(String key) {
        for (Pair<String, Object> pair : pairs) {
            if (key == pair.getKey()) {//key 是接口中的常量  可以用 ==
                return pair.getValue();
            }
        }
        return null;
    }

    @Override
    public void set(String key, Object value) {
        pairs.add(new Pair<>(key, value));
    }

    @Override
    public Iterator<Pair<String, Object>> iterator() {
        return pairs.iterator();
    }
}
