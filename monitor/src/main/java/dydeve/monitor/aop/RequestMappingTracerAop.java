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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * log host ip, remote ip, refer, url...
 * {@code RequestMapping} and {@code Trace}
 * Created by dy on 2017/8/2.
 */
@Component
@Aspect
public class RequestMappingTracerAop extends AbstractTracerAop {

    private static final Logger log = LoggerFactory.getLogger(RequestMappingTracerAop.class);

    @Autowired
    private KafkaSender sender;

    @Pointcut("(@target(org.springframework.stereotype.Controller) || @target(org.springframework.web.bind.annotation.RestController)) && @annotation(org.springframework.web.bind.annotation.RequestMapping) && @annotation(trace)")
    public void requestMappingTrace(Trace trace) {

    }

    @Pointcut("(@target(org.springframework.stereotype.Controller) || @target(org.springframework.web.bind.annotation.RestController)) && @annotation(org.springframework.web.bind.annotation.RequestMapping) && this(dydeve.monitor.aop.Traceable)")
    public void requestMappingTraceable() {

    }

    @Around(value = "requestMappingTraceable()")
    public Object traceableAround(ProceedingJoinPoint pjp) throws Throwable {
        return handle(pjp, ((MethodSignature) pjp.getSignature()).getMethod().getName(), true, true);

    }

    @Around(value = "requestMappingTrace(trace)")
    public Object traceAround(ProceedingJoinPoint pjp, Trace trace) throws Throwable {
        return handle(pjp, trace.description(), trace.recordParams(), trace.recordResult());

    }

    private Object handle(ProceedingJoinPoint pjp, String description, boolean recordParams, boolean recordResult) throws Throwable {
        Tracer<Map.Entry<String, Object>, String, MapStat> tracer = ThreadLocalHolder.TRACER.get();
        //Stopwatch stopwatch = ThreadLocalHolder.STOP_WATCH.get();//don't call reset()

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

        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        //stat.setAttribute(IStat.url, servletRequestAttributes.getRequest().getRequestURI());

        logRequest(stat, servletRequestAttributes.getRequest());

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

            afterReturn(stat, watch);
            //todo 测试异常时的status
            stat.setAttribute(IStat.status, servletRequestAttributes.getResponse().getStatus());
            if (traceFromHere) {
                sender.send(tracer);
                ThreadLocalHolder.TRACER.remove();
            }

        }
    }

    private void logRequest(MapStat stat, HttpServletRequest request) {
        if (request.getHeader("refer") != null) {
            stat.setAttribute(IStat.refer, request.getHeader("refer"));
        }
        if (request.getQueryString() != null) {
            stat.setAttribute(IStat.queryString, request.getQueryString());
        }
        stat.setAttribute(IStat.route, request.getRequestURI());
        stat.setAttribute(IStat.client, IPUtils.getIpAddress(request));
    }

}
