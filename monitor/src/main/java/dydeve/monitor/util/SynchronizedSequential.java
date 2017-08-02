package dydeve.monitor.util;

/**
 * Created by dy on 2017/8/2.
 */
public abstract class SynchronizedSequential<T> implements Sequential<T> {

    @Override
    public synchronized Sequencer<T> sequencer() {
        throw new UnsupportedOperationException();
    }
}
