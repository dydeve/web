package dydeve.monitor.aop;

import dydeve.monitor.aop.annotation.StopWatchHere;
import dydeve.monitor.util.StopWatchMonitor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by yuduy on 2017/7/29.
 */
@Aspect
public class StopWatcher {

    //todo custom async log
    private static final Logger log = LoggerFactory.getLogger(StopWatcher.class);

    @Pointcut(value = "@annotation(stopWatchHere)")//dydeve.monitor.aop.annotation.StopWatchHere
    public void stop(StopWatchHere stopWatchHere) {

    }

    @Around(value = "stop(stopWatchHere)")
    public Object watch(ProceedingJoinPoint pjp, StopWatchHere stopWatchHere) throws Throwable {
        /*String description;
        if (stopWatchHere.description() != null) {
            description = stopWatchHere.description();
        } else {
            description = ((MethodSignature) pjp.getSignature()).getName();
        }*/

        Object result;
        StopWatchMonitor monitor = StopWatchMonitor
                .createUnstarted()
                .setDescription(stopWatchHere.description());

        if (stopWatchHere.recordParams()) {
            monitor.setParams(pjp.getArgs());
        }

        try {
            monitor.start();
            result = pjp.proceed();
            monitor.stop();
            if (stopWatchHere.recordResult()) {
                monitor.setResult(result);
            }
            return result;
        } catch (Exception e) {
            monitor.stopWithException(e);
            if (stopWatchHere.throwException()) {
                throw e;
            }
            return null;
        } finally {
            //todo log.async(monitor);
        }
    }

}