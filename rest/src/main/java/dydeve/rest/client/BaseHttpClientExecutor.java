package dydeve.rest.client;

import dydeve.monitor.aop.Trace;
import dydeve.rest.request.HttpRequestFactory;
import dydeve.rest.response.CommonResponseConsumer;
import org.apache.http.Consts;
import org.apache.http.NameValuePair;
import org.apache.http.client.ResponseHandler;
import org.apache.http.impl.client.CloseableHttpClient;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.http.HttpMethod;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

/**
 * Created by dy on 2017/8/8.
 */
public class BaseHttpClientExecutor extends AbstractHttpClientExecutor implements InitializingBean {

    private CloseableHttpClient httpClient;

    public BaseHttpClientExecutor(CloseableHttpClient httpClient) {
        this.httpClient = httpClient;
    }

    @Trace
    @Override
    public String rtnString(String url, HttpMethod httpMethod, List<NameValuePair> params) throws IOException, URISyntaxException {
        return CommonResponseConsumer.returnString(
                httpClient,
                HttpRequestFactory.completedRequest(url, httpMethod, params, Consts.UTF_8));
    }

    @Trace
    @Override
    public byte[] rtnByteArray(String url, HttpMethod httpMethod, List<NameValuePair> params) throws IOException, URISyntaxException {
        return CommonResponseConsumer.returnByteArray(
                httpClient,
                HttpRequestFactory.completedRequest(url, httpMethod, params, Consts.UTF_8));
    }

    @Trace
    @Override
    public <T> T rtnObject(String url, HttpMethod httpMethod, List<NameValuePair> params, ResponseHandler<T> handler) throws IOException, URISyntaxException {
        return CommonResponseConsumer.returnObject(
                httpClient,
                HttpRequestFactory.completedRequest(url, httpMethod, params, Consts.UTF_8),
                handler);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        assert this.httpClient != null;
    }
}
