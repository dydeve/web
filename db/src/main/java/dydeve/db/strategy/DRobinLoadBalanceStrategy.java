package dydeve.db.strategy;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by dy on 2017/8/29.
 */
@Deprecated
public class DRobinLoadBalanceStrategy<T> implements LoadBalanceStrategy<T>{

    private final AtomicInteger counter = new AtomicInteger(-1);

    private final Lock lock = new ReentrantLock();

    //as big as possible
    private final int cycle;

    private final T[] sources;

    private final int length;

    public DRobinLoadBalanceStrategy(List<T> sources, int cycle) {

        if (sources == null || sources.size() < 2) {
            throw new IllegalArgumentException("the size of sources can't less than 2");
        }

        if (cycle < Integer.MAX_VALUE / 10 || cycle > (Integer.MAX_VALUE /10) * 9) {
            //if cycle is too small, the chosenIndex may beyond the cycle too frequently;
            //if cycle is too big,   the counter.incrementAndGet() may beyond the Integer.MAX_VALUE;
            //both of the above cases are unsafe
            throw new IllegalArgumentException("the cycle is too small or big");
        }
        this.sources = (T[]) sources.toArray();
        this.length = this.sources.length;
        this.cycle = cycle;

    }

    @Override
    public T chose() {
        return sources[chosenIndex() % length];
    }

    private int chosenIndex() {
        //chosenIndex:[0, cycle - 1]
        int chosenIndex = counter.incrementAndGet();

        if (chosenIndex >= cycle) {
            if (chosenIndex == cycle) {//to avoid syncing
                //the one who arrive on the end of cycle
                counter.set(-1);
                //ca't set to cycle - 1; remember —— we are in concurrency
                chosenIndex = counter.incrementAndGet();
            } else {
                //those who beyond the end of cycyle
                chosenIndex = counter.incrementAndGet();
                while (chosenIndex >= cycle) {
                    chosenIndex = counter.incrementAndGet();
                }
            }
        }

        return chosenIndex;
    }

    /*@Override
    public T chose() {
        int choseIndex = counter.getAndIncrement();
        if (choseIndex == cycle) {
            if (counter.get() == cycle + 1) {
                //the one who ar

            }
            lock.lock();
            try {

            } finally {
                lock.unlock();
            }
        }
        return null;
    }*/
}
