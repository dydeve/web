package dydeve.rest.client;

import org.apache.http.NameValuePair;
import org.springframework.http.HttpMethod;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

/**
 * Created by dy on 2017/8/8.
 */
public class BaseHttpClientExecutor extends AbstractHttpClientExecutor {
    @Override
    public <T> T execute(String url, HttpMethod httpMethod, Class<T> clazz) {
        return null;
    }

    @Override
    public <T> T execute(String url, HttpMethod httpMethod, Type type) {
        return null;
    }

    @Override
    public <T> T execute(String url, HttpMethod httpMethod, Class<T> clazz, Object param) {
        return null;
    }

    @Override
    public <T> T execute(String url, HttpMethod httpMethod, Type type, Object param) {
        return null;
    }

    @Override
    public <T> T execute(String url, HttpMethod httpMethod, Class<T> clazz, Object... params) {
        return null;
    }

    @Override
    public <T> T execute(String url, HttpMethod httpMethod, Type type, Object... params) {
        return null;
    }

    @Override
    public <T> T execute(String url, HttpMethod httpMethod, Class<T> clazz, Map<String, Object> params) {
        return null;
    }

    @Override
    public <T> T execute(String url, HttpMethod httpMethod, Type type, Map<String, Object> params) {
        return null;
    }

    @Override
    public <T> T execute(String url, HttpMethod httpMethod, Class<T> clazz, NameValuePair... params) {
        return null;
    }

    @Override
    public <T> T execute(String url, HttpMethod httpMethod, Type type, NameValuePair... params) {
        return null;
    }

    @Override
    public <T> T execute(String url, HttpMethod httpMethod, Class<T> clazz, List<NameValuePair> params) {
        return null;
    }

    @Override
    public <T> T execute(String url, HttpMethod httpMethod, Type type, List<NameValuePair> params) {
        return null;
    }
}
