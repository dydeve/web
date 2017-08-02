package dydeve.monitor.stat;

import com.google.common.base.Preconditions;

/**
 * @see com.google.common.base.Stopwatch
 * Created by dy on 2017/7/29.
 */
@Deprecated
public class StopWatch {

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


    public static StopWatch createUnstarted() {
        return new StopWatch();
    }

    public static StopWatch createStarted() {
        return new StopWatch().start();
    }

    /**
     * start with params
     * @param params
     * @return
     */
    public static StopWatch createStartedWithParams(Object ... params) {
        return createStarted().setParams(params);
    }

    public boolean isRunning() {
        return state == RUNNING;
    }

    public StopWatch start() {
        Preconditions.checkState(state == INIT, "this stop watch is already running");
        state = RUNNING;
        startTime = System.nanoTime();
        return this;
    }

    /**
     * stop with a given state
     * @return
     */
    public StopWatch stop(Boolean s) {
        elapsedNanos = System.nanoTime() - startTime;
        Preconditions.checkState(state = RUNNING, "This stopwatch is already stopped.");
        this.state = s;
        return this;
    }

    public StopWatch stop() {
        return stop(SUCCESS);
    }

    public StopWatch stopInnormally() {
        return stop(FAIL);
    }

    public StopWatch stopWithException(Exception e) {
        return stop(FAIL).setE(e);
    }

    public StopWatch stopWithResult(Object result) {
        return stop(SUCCESS).setResult(result);
    }

    public String getDescription() {
        return description;
    }

    public StopWatch setDescription(String description) {
        this.description = description;
        return this;
    }

    public Exception getE() {
        return e;
    }

    public StopWatch setE(Exception e) {
        this.e = e;
        return this;
    }

    public Object[] getParams() {
        return params;
    }

    public StopWatch setParams(Object[] params) {
        this.params = params;
        return this;
    }

    public Object getResult() {
        return result;
    }

    public StopWatch setResult(Object result) {
        this.result = result;
        return this;
    }

}
