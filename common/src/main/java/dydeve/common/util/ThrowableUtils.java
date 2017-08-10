package dydeve.common.util;

import org.springframework.boot.autoconfigure.web.DefaultErrorAttributes;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Map;

/**
 * Created by yuduy on 2017/8/7.
 */
public class ThrowableUtils {

    private ThrowableUtils() {
        throw new UnsupportedOperationException();
    }

    /**
     * @see DefaultErrorAttributes#addStackTrace(Map, Throwable)
     * @param throwable
     * @return
     */
    public static String stackTrace(Throwable throwable) {
        StringWriter stackTrace = new StringWriter();
        throwable.printStackTrace(new PrintWriter(stackTrace));
        stackTrace.flush();
        return stackTrace.toString();
    }

    public static Throwable getRootCause(Throwable throwable) {
        Throwable root = throwable;
        while (root.getCause() != null) {
            root = root.getCause();
        }
        return root;
    }

}
