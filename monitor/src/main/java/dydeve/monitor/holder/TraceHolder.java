package dydeve.monitor.holder;

import java.util.UUID;

/**
 * request trace holder consist of thread local
 * Created by duyu on 2017/7/24.
 */
public class TraceHolder {

    //private static final ThreadLocal<Monitor> TRACER = new NamedInheritableThreadLocal<>("TRACER");

    //uuid
    public static final ThreadLocal<String> TRACE_ID = new ThreadLocal<String>() {
        @Override
        protected String initialValue() {
            return UUID.randomUUID().toString();
        }
    };

    //request uri
    public static final ThreadLocal<String> REQUEST_URI = new ThreadLocal<String>() {
        @Override
        protected String initialValue() {
            return null;
        }
    };
}
