package dydeve.monitor.aop;

import dydeve.common.util.ThrowableUtils;
import dydeve.monitor.stat.IStat;
import dydeve.monitor.stat.MapStat;
import dydeve.monitor.stat.TimerWatch;
import org.aspectj.lang.ProceedingJoinPoint;

/**
 * Created by yuduy on 2017/8/10.
 */
public abstract class AbstractTracerAop {

    protected Object around(ProceedingJoinPoint pjp, boolean recordResult, MapStat stat, TimerWatch watch) throws Throwable {
        watch.start();
        Object result = pjp.proceed();
        watch.stop();

        after(recordResult, stat, result);
        return result;
    }

    protected void after(boolean recordResult, MapStat stat, Object result) {
        stat.setAttribute(IStat.level, "info");
        if (recordResult && result != null) {
            //todo test null and void
            stat.setAttribute(IStat.result, result);
        }
    }

    protected void afterThrow(MapStat stat, TimerWatch watch, Exception e) {
        watch.stop();
        stat.setAttribute(IStat.level, "error");
        stat.setAttribute(IStat.error, ThrowableUtils.stackTrace(e));
    }

    protected void afterReturn(MapStat stat, TimerWatch watch) {
        stat.setAttribute(IStat.startTime, watch.startTime());
        stat.setAttribute(IStat.elapsedTime, watch.elapsedTime());
    }

}
