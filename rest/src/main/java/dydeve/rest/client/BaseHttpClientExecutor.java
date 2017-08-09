package dydeve.rest.client;

import dydeve.common.util.CloseableUtils;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;

import java.io.IOException;
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

    /**
     *
     * @param url
     * @param params
     * @return
     * @throws URISyntaxException
     */
    public String get(String url, List<NameValuePair> params) throws URISyntaxException {
        HttpGet httpGet = new HttpGet();

        if (params == null) {
            httpGet.setURI(URI.create(url));
        } else {
            httpGet.setURI(new URIBuilder(url).addParameters(params).build());
        }

        CloseableHttpResponse response = null;
        try {
            response = httpClient.execute(httpGet);
            return EntityUtils.toString(response.getEntity());
        } catch (IOException e) {
            log.error("IOException here, url:{}",url, e);
            return null;
        } finally {
           CloseableUtils.closeQuietly(response);
        }

    }


}
