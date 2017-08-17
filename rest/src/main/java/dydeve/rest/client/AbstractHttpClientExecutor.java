package dydeve.rest.client;

import dydeve.rest.request.NameValuePairs;
import org.apache.http.NameValuePair;
import org.apache.http.client.ResponseHandler;
import org.springframework.http.HttpMethod;

import java.beans.IntrospectionException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;

/**
 * Created by dy on 2017/8/8.
 */
public abstract class AbstractHttpClientExecutor implements HttpClientExecutor {

    @Override
    public String getString(String url, Object param) throws IllegalAccessException, IntrospectionException, InvocationTargetException, IOException, URISyntaxException {
        return rtnString(url, HttpMethod.GET, param);
    }

    @Override
    public String getString(String url, Map<String, Object> params) throws IOException, URISyntaxException {
        return rtnString(url, HttpMethod.GET, params);
    }

    @Override
    public String getString(String url, List<NameValuePair> params) throws IOException, URISyntaxException {
        return rtnString(url, HttpMethod.GET, params);
    }

    @Override
    public byte[] getByteArray(String url, Object param) throws IllegalAccessException, IntrospectionException, InvocationTargetException, IOException, URISyntaxException {
        return rtnByteArray(url, HttpMethod.GET, param);
    }

    @Override
    public byte[] getByteArray(String url, Map<String, Object> params) throws IllegalAccessException, IntrospectionException, InvocationTargetException, IOException, URISyntaxException {
        return rtnByteArray(url, HttpMethod.GET, params);
    }

    @Override
    public byte[] getByteArray(String url, List<NameValuePair> params) throws IOException, URISyntaxException {
        return rtnByteArray(url, HttpMethod.GET, params);
    }

    @Override
    public <T> T getObject(String url, Object param, ResponseHandler<T> handler) throws IOException, URISyntaxException, IllegalAccessException, IntrospectionException, InvocationTargetException {
        return rtnObject(url, HttpMethod.GET, param, handler);
    }

    @Override
    public <T> T getObject(String url, Map<String, Object> params, ResponseHandler<T> handler) throws IOException, URISyntaxException {
        return rtnObject(url, HttpMethod.GET, params, handler);
    }

    @Override
    public <T> T getObject(String url, List<NameValuePair> params, ResponseHandler<T> handler) throws IOException, URISyntaxException {
        return rtnObject(url, HttpMethod.GET, params, handler);
    }



    @Override
    public String rtnString(String url, HttpMethod httpMethod, Object param) throws IllegalAccessException, IntrospectionException, InvocationTargetException, IOException, URISyntaxException {
        return rtnString(url, httpMethod, NameValuePairs.fromObject(param));
    }

    @Override
    public String rtnString(String url, HttpMethod httpMethod, Map<String, Object> params) throws IOException, URISyntaxException {
        return rtnString(url, httpMethod, NameValuePairs.fromMap(params));
    }

    @Override
    public byte[] rtnByteArray(String url, HttpMethod httpMethod, Object param) throws IllegalAccessException, IntrospectionException, InvocationTargetException, IOException, URISyntaxException {
        return rtnByteArray(url, httpMethod, NameValuePairs.fromObject(param));
    }

    @Override
    public byte[] rtnByteArray(String url, HttpMethod httpMethod, Map<String, Object> params) throws IllegalAccessException, IntrospectionException, InvocationTargetException, IOException, URISyntaxException {
        return rtnByteArray(url, httpMethod, NameValuePairs.fromMap(params));
    }

    @Override
    public <T> T rtnObject(String url, HttpMethod httpMethod, Object param, ResponseHandler<T> handler) throws IOException, URISyntaxException, IllegalAccessException, IntrospectionException, InvocationTargetException {
        return rtnObject(url, httpMethod, NameValuePairs.fromObject(param), handler);
    }

    @Override
    public <T> T rtnObject(String url, HttpMethod httpMethod, Map<String, Object> params, ResponseHandler<T> handler) throws IOException, URISyntaxException {
        return rtnObject(url, httpMethod, NameValuePairs.fromMap(params), handler);
    }
}
