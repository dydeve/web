package dydeve.db.strategy;

import java.util.List;
import java.util.function.Supplier;

/**
 * Created by dy on 2017/8/31.
 */
public class LoadBalanceStrategyFactory<S, T extends LoadBalanceStrategy<S>> {

    public T generate(List<S> sources, Supplier<T> supplier) {
        if (sources == null || sources.isEmpty()) {
            throw new IllegalArgumentException("source can't be empty");
        }
        if (sources.size() == 1) {
            return (T) new SingleLoadBalanceStrategy(sources);
        }
        return supplier.get();
    }

}
