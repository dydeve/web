package dydeve.monitor.aop;

import dydeve.monitor.holder.SingletonHolder;
import dydeve.monitor.holder.ThreadLocalHolder;
import dydeve.monitor.log.KafkaSender;
import dydeve.monitor.stat.*;
import dydeve.monitor.util.IPUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

/**
 * Created by yuduy on 2017/8/10.
 */
@Aspect
public class CommonTracerAop extends AbstractTracerAop{

    @Autowired
    private KafkaSender sender;

    @Pointcut("this(dydeve.monitor.aop.Traceable) && !@annotation(org.springframework.web.bind.annotation.RequestMapping)")
    public void commonTraceable() {

    }

    @Pointcut("@annotation(trace) && !@annotation(org.springframework.web.bind.annotation.RequestMapping)")
    public void commonTrace(Trace trace) {

    }


    @Around(value = "commonTraceable()")
    public Object traceableAround(ProceedingJoinPoint pjp) throws Throwable {
        return handle(pjp, ((MethodSignature) pjp.getSignature()).getMethod().getName(), true, true);
    }

    @Around(value = "commonTrace(trace)")
    public Object traceAround(ProceedingJoinPoint pjp, Trace trace) throws Throwable {
        return handle(
                pjp,
                trace.value() == Trace.NULL ? ((MethodSignature) pjp.getSignature()).getMethod().getName(): trace.value(),
                trace.recordParams(),
                trace.recordResult());
    }

    private Object handle(ProceedingJoinPoint pjp, String description, boolean recordParams, boolean recordResult) throws Throwable {
        Tracer<Map.Entry<String, Object>, String, MapStat> tracer = ThreadLocalHolder.TRACER.get();

        boolean traceFromHere = false;

        if (tracer == null) {
            tracer = new Tracer<>(
                    Tracer.Group.SITE,
                    ThreadLocalHolder.TRACE_ID.get(),
                    IPUtils.LOCAL_IP,
                    SingletonHolder.getMapStatFactory());

            ThreadLocalHolder.TRACER.set(tracer);

            traceFromHere = true;
        }

        MapStat stat = tracer.makeStat();

        stat.setAttribute(IStat.name, description);

        if (recordParams) {
            stat.setAttribute(IStat.params, pjp.getArgs());
        }

        TimerWatch watch = StopWatchTimerWatch.newInstance();
        try {
            return around(pjp, recordResult, stat, watch);
        } catch (Exception e) {
            afterThrow(stat, watch, e);
            throw e;
        } finally {
            //无 status
            afterReturn(stat, watch);
            if (traceFromHere) {
                sender.send(tracer);
                ThreadLocalHolder.TRACER.remove();
            }

        }
    }


}
