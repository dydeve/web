package dydeve.common.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Closeable;
import java.io.IOException;

/**
 * Created by yuduy on 2017/8/9.
 */
public class CloseableUtils {

    private static final Logger log = LoggerFactory.getLogger(CloseableUtils.class);

    private CloseableUtils() {
        throw new UnsupportedOperationException();
    }

    public static void closeQuietly(Closeable closeable) {
        if (closeable == null) {
            return;
        }
        try {
            closeable.close();
        } catch (IOException e) {
            log.error("IOException thrown while closing Closeable.", e);
        }
    }

    public static void close(Closeable closeable) throws IOException {
        if (closeable == null) {
            return;
        }
        closeable.close();
    }

}
