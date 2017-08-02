package dydeve.monitor.stat;

/**
 * 计时器
 * Created by yuduy on 2017/8/2.
 */
public interface TimerWatch {

    void start();

    void stop();

    long startTime();

    /**
     * run time
     * @return
     */
    long elapsedTime();

}
