package dydeve.monitor.util;

import java.util.Collection;

/**
 * common snapshot
 * K key type
 * V value type
 * E iterator's entity
 * T transfer to
 * Created by dy on 2017/8/2.
 */
public interface SnapShot<K, V, E, T> extends Transferable<T>, Iterable<E> {

    void set(K key, V value);

    V get(K key);

    Collection<K> keys();

}
