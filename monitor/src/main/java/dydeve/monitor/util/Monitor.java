package dydeve.monitor.util;

import com.google.common.base.Preconditions;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * not thread safe
 * @see org.springframework.util.StopWatch
 * @see com.google.common.base.Stopwatch
 * Created by yuduy on 2017/7/24.
 */
public class Monitor {

    /**
     * Identifier of this stop watch.
     * Handy when we have output from multiple stop watches
     * and need to distinguish between them in log or console output.
     */
    private String id;
    private boolean keepTaskList = false;
    private List<TaskInfo> taskList = null;

    private long startTime;//nanos
    private long totalTime;//nanos

    //we cache monitor in threadLocal,only when threadLocal.get() == null can we stop!
    private boolean running = false;

    private AtomicInteger step = new AtomicInteger(0);//step start from 1

    Monitor(String id) {
        this.id = id;
    }

    public static Monitor createUnstarted(String id) {
        return new Monitor(id);
    }

    public static Monitor createStarted(String id) {
        return new Monitor(id).start();
    }

    public Monitor start() {
        Preconditions.checkState(!running, "This stopwatch is already running.");

        running = true;
        startTime = System.nanoTime();
        return this;
    }

    /**
     *
     */
    public void stop() {

    }

    public void setKeepTaskList(boolean keepTaskList) {
        this.keepTaskList = keepTaskList;
    }

    private static class TaskInfo {

        private

    }
}
