package dydeve.common.util;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Utility to have 'intern' - like functionality, which holds single instance of wrapper for a given key
 * Created by yuduy on 2017/8/17.
 */
public class InternMapStorage<K, V> implements InternStorage<K, V> {

    private final Map<K, V> storage;
    private final InternStorage.ValueConstructor<K, V> valueConstructor;

    private InternMapStorage(Map<K, V> storage, InternStorage.ValueConstructor<K, V> valueConstructor) {
        this.storage = storage;
        this.valueConstructor = valueConstructor;
    }

    public static InternMapStorage of(boolean threadSafe, InternStorage.ValueConstructor valueConstructor) {
        if (threadSafe) {
            return new InternMapStorage(new ConcurrentHashMap<>(), valueConstructor);
        }
        return new InternMapStorage(new HashMap<>(), valueConstructor);
    }

    @Override
    public V interned(K key) {
        V existingKey = storage.get(key);
        V newKey = null;
        if (existingKey == null) {
            newKey = valueConstructor.create(key);
            existingKey = storage.putIfAbsent(key, newKey);
        }
        return existingKey != null ? existingKey : newKey;
    }

    @Override
    public int size() {
        return storage.size();
    }

}
