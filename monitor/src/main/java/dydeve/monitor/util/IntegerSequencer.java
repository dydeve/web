package dydeve.monitor.util;

/**
 * not thread safe.
 * if need thread safe, use java.util.concurrent.atomic.* instead
 * Created by dy on 2017/8/2.
 */
public class IntegerSequencer extends NumberSequencer<Integer> {

    public IntegerSequencer() {
        super(0);
    }

    public IntegerSequencer(Integer initValue) {
        super(initValue);
    }

    public static IntegerSequencer newInstance() {
        return new IntegerSequencer();
    }

    @Override
    public Integer getAndNext() {
        return value ++;
    }

    @Override
    public Integer nextAndGet() {
        return ++ value;
    }

}
