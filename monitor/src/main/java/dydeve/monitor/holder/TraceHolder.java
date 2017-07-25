package dydeve.monitor.holder;

import dydeve.monitor.util.Monitor;
import org.springframework.core.NamedInheritableThreadLocal;
import org.springframework.core.NamedThreadLocal;

/**
 * request trace holder consist of thread local
 * Created by duyu on 2017/7/24.
 */
public class TraceHolder {

    private static final ThreadLocal<Monitor> TRACER = new NamedInheritableThreadLocal<>("TRACER");

    //uuid
    public static final ThreadLocal<String> TRACE_ID = new ThreadLocal<String>() {
        @Override
        protected String initialValue() {
            throw new NullPointerException("can't get traceId from initialValue");
        }
    };

    //request uri
    public static final ThreadLocal<String> REQUEST_URI = new ThreadLocal<String>() {
        @Override
        protected String initialValue() {
            throw new NullPointerException("can't get traceId from initialValue");
        }
    };
}
