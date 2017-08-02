package dydeve.monitor.util;

/**
 * Created by dy on 2017/8/2.
 */
public abstract class SynchronizedSequential<T> implements Sequential<T> {

    @Override
    public synchronized T getAndNext() {
        throw new UnsupportedOperationException();
    }

    @Override
    public synchronized T nextAndGet() {
        throw new UnsupportedOperationException();
    }

}
