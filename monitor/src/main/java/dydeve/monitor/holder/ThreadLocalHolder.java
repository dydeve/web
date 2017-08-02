package dydeve.monitor.holder;

import dydeve.monitor.stat.MapStat;
import dydeve.monitor.stat.Tracer;
import org.springframework.core.NamedThreadLocal;

import java.util.Map;
import java.util.UUID;

/**
 * threadLocal holder
 * Created by dy on 2017/7/24.
 */
public class ThreadLocalHolder {

    public static final ThreadLocal<Tracer<Map.Entry<String, Object>, String, MapStat>> TRACER = new NamedThreadLocal<Tracer<Map.Entry<String, Object>, String, MapStat>>("tracer threadLocal") {
        @Override
        protected Tracer<Map.Entry<String, Object>, String, MapStat> initialValue() {
            return null;
        }
    };

    /*@NotThreadSafe
    public static final ThreadLocal<Stopwatch> STOP_WATCH = new NamedThreadLocal<Stopwatch>("stopWatch threadLocal") {
        @Override
        protected Stopwatch initialValue() {
            return null;
        }
    };*/

    //we don't return null because the interceptor may not in used
    public static final ThreadLocal<String> TRACE_ID = new NamedThreadLocal<String>("traceId threadLocal") {
        @Override
        protected String initialValue() {
            return UUID.randomUUID().toString();
        }
    };

}
