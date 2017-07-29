package dydeve.monitor.util;

import com.google.common.base.Preconditions;

/**
 * @see com.google.common.base.Stopwatch
 * Created by yuduy on 2017/7/29.
 */
public class StopWatchMonitor {

    private static final Boolean SUCCESS = Boolean.TRUE;
    private static final Boolean FAIL = Boolean.FALSE;
    private static final Boolean INIT = null;
    private static final Boolean RUNNING = new Boolean(true);

    private String description;
    private Exception e;
    private Object[] params;
    private Object result;

    private Boolean state;//null -> init state  ;  TRUE -> success  ;  FALSE -> fail

    private long startTime;
    private long elapsedNanos;


    public static StopWatchMonitor createUnstarted() {
        return new StopWatchMonitor();
    }

    public static StopWatchMonitor createStarted() {
        return new StopWatchMonitor().start();
    }

    /**
     * start with params
     * @param params
     * @return
     */
    public static StopWatchMonitor createStartedWithParams(Object ... params) {
        return createStarted().setParams(params);
    }

    public boolean isRunning() {
        return state == RUNNING;
    }

    public StopWatchMonitor start() {
        Preconditions.checkState(state == INIT, "this stop watch is already running");
        state = RUNNING;
        startTime = System.nanoTime();
        return this;
    }

    /**
     * stop with a given state
     * @return
     */
    public StopWatchMonitor stop(Boolean s) {
        elapsedNanos = System.nanoTime() - startTime;
        Preconditions.checkState(state = RUNNING, "This stopwatch is already stopped.");
        this.state = s;
        return this;
    }

    public StopWatchMonitor stop() {
        return stop(SUCCESS);
    }

    public StopWatchMonitor stopInnormally() {
        return stop(FAIL);
    }

    public StopWatchMonitor stopWithException(Exception e) {
        return stop(FAIL).setE(e);
    }

    public StopWatchMonitor stopWithResult(Object result) {
        return stop(SUCCESS).setResult(result);
    }

    public String getDescription() {
        return description;
    }

    public StopWatchMonitor setDescription(String description) {
        this.description = description;
        return this;
    }

    public Exception getE() {
        return e;
    }

    public StopWatchMonitor setE(Exception e) {
        this.e = e;
        return this;
    }

    public Object[] getParams() {
        return params;
    }

    public StopWatchMonitor setParams(Object[] params) {
        this.params = params;
        return this;
    }

    public Object getResult() {
        return result;
    }

    public StopWatchMonitor setResult(Object result) {
        this.result = result;
        return this;
    }

}
