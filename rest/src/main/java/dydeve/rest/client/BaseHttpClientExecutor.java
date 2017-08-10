package dydeve.rest.client;

import dydeve.common.reflect.TypeReference;
import dydeve.common.util.CloseableUtils;
import dydeve.monitor.aop.Trace;
import dydeve.rest.request.HttpRequestFactory;
import dydeve.rest.response.CommonResponseConsumer;
import dydeve.rest.response.JsonResponseHandler;
import org.apache.http.Consts;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;

/**
 * Created by dy on 2017/8/8.
 */
public class BaseHttpClientExecutor extends AbstractHttpClientExecutor {

    private static final Logger log = LoggerFactory.getLogger(BaseHttpClientExecutor.class);

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

    @Autowired
    private CloseableHttpClient httpClient;


    public HttpUriRequest completedRequest(String url, HttpMethod httpMethod, List<NameValuePair> params) throws URISyntaxException, UnsupportedEncodingException {

        HttpUriRequest httpUriRequest = HttpRequestFactory.createHttpRequest(httpMethod);

        if (HttpMethod.GET == httpMethod) {
            URIBuilder uriBuilder = new URIBuilder(url);
            if (params != null) {
                uriBuilder.addParameters(params);
            }
            ((HttpGet) httpUriRequest).setURI(uriBuilder.build());
            return httpUriRequest;
        }

        if (HttpMethod.POST == httpMethod) {
            ((HttpPost) httpUriRequest).setURI(URI.create(url));
            if (params != null) {
                UrlEncodedFormEntity entity = new UrlEncodedFormEntity(params, Consts.UTF_8);
                ((HttpPost) httpUriRequest).setEntity(entity);
            }
            return httpUriRequest;
        }

        //ignore
        throw new UnsupportedOperationException();
    }

    public String produceString(String url, HttpMethod httpMethod, List<NameValuePair> params) throws IOException, URISyntaxException {
        return CommonResponseConsumer.returnString(httpClient, completedRequest(url, httpMethod, params));
    }

    public byte[] produceByteArray(String url, HttpMethod httpMethod, List<NameValuePair> params) throws IOException, URISyntaxException {
        return CommonResponseConsumer.returnByteArray(httpClient, completedRequest(url, httpMethod, params));
    }

    public <T> T produceObject(String url, HttpMethod httpMethod, List<NameValuePair> params, JsonResponseHandler<T> handler) throws IOException, URISyntaxException {
        return CommonResponseConsumer.returnObject(httpClient,
                completedRequest(url, httpMethod, params),
                handler);
    }

}
