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

    public static Monitor get() {
        return TRACER.get();
    }

    public static void set(Monitor monitor) {
        TRACER.set(monitor);
    }

    /**
     * important! must remove finally
     */
    public static void clear() {
        TRACER.remove();
    }

}
