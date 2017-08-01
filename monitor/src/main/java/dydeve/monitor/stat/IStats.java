package dydeve.monitor.stat;

import java.util.Collection;

/**
 * stats interface
 * Created by dy on 2017/8/1.
 */
public interface IStats<T> extends Iterable<T>{

    String startTime = "startTime";
    String elapsedTime = "elapsedTime";
    String status = "status";
    String level = "level";

    String host = "host";
    String client = "client";
    String url = "url";
    String refer = "refer";

    String error = "error";


    Collection<String> keys();

    Object get(String key);

    void set(String key, Object value);

}
