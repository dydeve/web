package dydeve.db.strategy;

import java.util.List;
import java.util.Objects;

/**
 * Created by dy on 2017/8/30.
 */
public class SingleLoadBalanceStrategy<T> implements LoadBalanceStrategy<T> {

    private final T source;

    public SingleLoadBalanceStrategy(T source) {
        this.source = Objects.requireNonNull(source);
    }

    public SingleLoadBalanceStrategy(List<T> sources) {
        if (sources == null || sources.size() != 1) {
            throw new IllegalArgumentException("sources's size must be 1");
        }
        this.source = sources.get(0);
    }

    @Override
    public T chose() {
        return source;
    }
}
