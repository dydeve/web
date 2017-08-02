package dydeve.monitor.util;

/**
 * number sequence
 * Created by dy on 2017/8/2.
 */
public abstract class NumberSequencer<N extends Number> implements Sequencer<N> {

    protected N value;

    public NumberSequencer() {

    }

    public NumberSequencer(N initValue) {
        this.value = initValue;
    }

    /*protected void init(N value) {
        this.value = value;
    }*/

}
