package dydeve.db.strategy;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by yuduy on 2017/9/4.
 */
public class RobinLoadBalanceStrategy<T> implements LoadBalanceStrategy<T> {

    private final AtomicInteger counter = new AtomicInteger(-1);

    private final Lock lock = new ReentrantLock();

    private final T[] sources;

    private final int length;

    public RobinLoadBalanceStrategy(List<T> sources) {

        if (sources == null || sources.size() < 2) {
            throw new IllegalArgumentException("the size of sources can't less than 2");
        }
        this.sources = (T[]) sources.toArray();
        this.length = this.sources.length;
    }

    @Override
    public T chose() {
        return sources[Math.abs(counter.incrementAndGet()) % length];
    }
}

