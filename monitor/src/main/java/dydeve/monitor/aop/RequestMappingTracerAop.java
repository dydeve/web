package dydeve.monitor.aop;

import dydeve.common.ThrowableUtils;
import dydeve.monitor.aop.annotation.Trace;
import dydeve.monitor.holder.SingletonHolder;
import dydeve.monitor.holder.ThreadLocalHolder;
import dydeve.monitor.log.KafkaSender;
import dydeve.monitor.stat.*;
import dydeve.monitor.util.IPUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Map;

/**
 * {@code RequestMapping} and {@code Trace}
 * Created by dy on 2017/8/2.
 */
@Component
@Aspect
public class RequestMappingTracerAop {

    private static final Logger log = LoggerFactory.getLogger(RequestMappingTracerAop.class);

    @Autowired
    private KafkaSender sender;

    @Pointcut("@annotation(requestMapping) && @annotation(trace)")
    public void requestMappingTrace(RequestMapping requestMapping, Trace trace) {

    }

    @Around(value = "requestMappingTrace(requestMapping, trace)")
    public Object traceAround(ProceedingJoinPoint pjp, RequestMapping requestMapping, Trace trace) throws Throwable {
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

        stat.setAttribute(IStat.name, trace.description());

        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        //stat.setAttribute(IStat.url, servletRequestAttributes.getRequest().getRequestURI());

        logRequest(stat, servletRequestAttributes.getRequest());

        if (trace.recordParams()) {
            stat.setAttribute(IStat.params, pjp.getArgs());
        }

        TimerWatch watch = StopWatchTimerWatch.newInstance();
        Object result;
        try {
            watch.start();
            result = pjp.proceed();
            watch.stop();

            stat.setAttribute(IStat.level, "info");
            if (trace.recordResult() && result != null) {
                //todo test null and void
                stat.setAttribute(IStat.result, result);
            }
            return result;
        } catch (Exception e) {
            watch.stop();
            stat.setAttribute(IStat.level, "error");
            stat.setAttribute(IStat.error, ThrowableUtils.stackTrace(e));
            throw e;
        } finally {

            stat.setAttribute(IStat.startTime, watch.startTime());
            stat.setAttribute(IStat.elapsedTime, watch.elapsedTime());
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
