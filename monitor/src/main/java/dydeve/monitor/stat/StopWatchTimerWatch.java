package dydeve.monitor.stat;

/**
 * simplify {@link com.google.common.base.Stopwatch}
 * Created by dy on 2017/8/2.
 */
public class StopWatchTimerWatch implements TimerWatch {

    private long startTime;
    private long elapsedTime;

    public static StopWatchTimerWatch newInstance() {
        return new StopWatchTimerWatch();
    }

    @Override
    public void start() {
        this.startTime = System.currentTimeMillis();
    }

    @Override
    public void stop() {
        this.elapsedTime = System.currentTimeMillis() - startTime;
    }

    @Override
    public long startTime() {
        return this.startTime;
    }

    @Override
    public long elapsedTime() {
        return this.elapsedTime;
    }
}
