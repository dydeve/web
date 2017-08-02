package dydeve.monitor.stat;

import dydeve.monitor.util.SnapShoter;

import java.util.Collection;

/**
 * stats interface
 * Created by dy on 2017/8/1.
 */
public interface IStat<E, T> extends SnapShoter<String, Object, E, T> {

    String startTime = "startTime";
    String elapsedTime = "elapsedTime";
    String status = "status";
    String level = "level";

    String host = "host";
    String client = "client";
    String url = "url";
    String refer = "refer";

    String error = "error";

    String sequence = "sequence";

    String params = "params";

    String result = "result";

    String name = "name";


    Collection<String> attributeNames();

    Object getAttributeValue(String attributeName);

    void setAttribute(String attributeName, Object attributeValue);


}
