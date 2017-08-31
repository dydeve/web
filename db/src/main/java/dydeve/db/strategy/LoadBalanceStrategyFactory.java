package dydeve.db.strategy;

import java.util.List;
import java.util.function.Supplier;

/**
 * Created by dy on 2017/8/31.
 */
public class LoadBalanceStrategyFactory<T> {

    public LoadBalanceStrategy<T> generate(List<T> sources, Supplier<LoadBalanceStrategy<T>> supplier) {
        if (sources == null || sources.isEmpty()) {
            throw new IllegalArgumentException("source can't be empty");
        }
        if (sources.size() == 1) {
            return new SingleLoadBalanceStrategy<T>(sources);
        }
        return supplier.get();
    }

}
