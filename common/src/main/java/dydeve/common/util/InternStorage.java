package dydeve.common.util;

/**
 * @see com.netflix.hystrix.util.InternMap
 * Created by yuduy on 2017/8/17.
 */
public interface InternStorage<K, V>{

    V interned(K key);

    int size();

    interface ValueConstructor<K, V> {
        V create(K key);
    }

}
