package dydeve.monitor.util;

/**
 * Created by dy on 2017/8/2.
 */
public interface Sequencer<S> {

    S getAndNext();

    S nextAndGet();

}
