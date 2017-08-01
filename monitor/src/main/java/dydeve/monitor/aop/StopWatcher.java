package dydeve.monitor.aop;

import dydeve.monitor.aop.annotation.StopWatchHere;
import dydeve.monitor.log.KafkaSender;
import dydeve.monitor.stat.StopWatch;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by yuduy on 2017/7/29.
 */
@Component
@Aspect
public class StopWatcher {

    @Autowired
    private KafkaSender sender;

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
        StopWatch monitor = StopWatch
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
            sender.send(monitor);
        }
    }

}