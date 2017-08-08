package dydeve.rest.client;

import org.apache.http.NameValuePair;
import org.springframework.http.HttpMethod;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

/**
 * Created by dy on 2017/8/8.
 */
public interface HttpClientExecutor {

    public <T> T get(String url, Class<T> clazz);

    public <T> T get(String url, Type type);

    public <T> T get(String url, Class<T> clazz, Object param);

    public <T> T get(String url, Type type, Object param);

    public <T> T get(String url, Class<T> clazz, Object ...params);

    public <T> T get(String url, Type type, Object ...params);

    public <T> T get(String url, Class<T> clazz, Map<String, Object> params);

    public <T> T get(String url, Type type, Map<String, Object> params);

    public <T> T get(String url, Class<T> clazz, NameValuePair... params);

    public <T> T get(String url, Type type, NameValuePair... params);

    public <T> T get(String url, Class<T> clazz, List<NameValuePair> params);

    public <T> T get(String url, Type type, List<NameValuePair> params);


    public <T> T execute(String url, HttpMethod httpMethod, Class<T> clazz);

    public <T> T execute(String url, HttpMethod httpMethod, Type type);

    public <T> T execute(String url, HttpMethod httpMethod, Class<T> clazz, Object param);

    public <T> T execute(String url, HttpMethod httpMethod, Type type, Object param);

    public <T> T execute(String url, HttpMethod httpMethod, Class<T> clazz, Object ...params);

    public <T> T execute(String url, HttpMethod httpMethod, Type type, Object ...params);

    public <T> T execute(String url, HttpMethod httpMethod, Class<T> clazz, Map<String, Object> params);

    public <T> T execute(String url, HttpMethod httpMethod, Type type, Map<String, Object> params);

    public <T> T execute(String url, HttpMethod httpMethod, Class<T> clazz, NameValuePair... params);

    public <T> T execute(String url, HttpMethod httpMethod, Type type, NameValuePair... params);

    public <T> T execute(String url, HttpMethod httpMethod, Class<T> clazz, List<NameValuePair> params);

    public <T> T execute(String url, HttpMethod httpMethod, Type type, List<NameValuePair> params);
}
