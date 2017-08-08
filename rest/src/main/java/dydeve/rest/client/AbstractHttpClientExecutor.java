package dydeve.rest.client;

import org.apache.http.NameValuePair;
import org.springframework.http.HttpMethod;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

/**
 * Created by dy on 2017/8/8.
 */
public abstract class AbstractHttpClientExecutor implements HttpClientExecutor {
    @Override
    public <T> T get(String url, Class<T> clazz) {
        return execute(url, HttpMethod.GET, clazz);
    }

    @Override
    public <T> T get(String url, Type type) {
        return execute(url, HttpMethod.GET, type);
    }

    @Override
    public <T> T get(String url, Class<T> clazz, Object param) {
        return execute(url, HttpMethod.GET, clazz, param);
    }

    @Override
    public <T> T get(String url, Type type, Object param) {
        return execute(url, HttpMethod.GET, type, param);
    }

    @Override
    public <T> T get(String url, Class<T> clazz, Object... params) {
        return execute(url, HttpMethod.GET, clazz, params);
    }

    @Override
    public <T> T get(String url, Type type, Object... params) {
        return execute(url, HttpMethod.GET, type, params);
    }

    @Override
    public <T> T get(String url, Class<T> clazz, Map<String, Object> params) {
        return execute(url, HttpMethod.GET, clazz, params);
    }

    @Override
    public <T> T get(String url, Type type, Map<String, Object> params) {
        return execute(url, HttpMethod.GET, type, params);
    }

    @Override
    public <T> T get(String url, Class<T> clazz, NameValuePair... params) {
        return execute(url, HttpMethod.GET, clazz, params);
    }

    @Override
    public <T> T get(String url, Type type, NameValuePair... params) {
        return execute(url, HttpMethod.GET, type, params);
    }

    @Override
    public <T> T get(String url, Class<T> clazz, List<NameValuePair> params) {
        return execute(url, HttpMethod.GET, clazz, params);
    }

    @Override
    public <T> T get(String url, Type type, List<NameValuePair> params) {
        return execute(url, HttpMethod.GET, type, params);
    }

}
