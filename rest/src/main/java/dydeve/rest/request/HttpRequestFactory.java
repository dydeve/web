package dydeve.rest.request;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.*;
import org.apache.http.client.utils.URIBuilder;
import org.springframework.http.HttpMethod;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.util.List;

/**
 * Created by dy on 2017/8/9.
 */
public class HttpRequestFactory {


    public static HttpRequestBase createHttpRequest(HttpMethod httpMethod) {
        switch (httpMethod) {
            case GET:
                return new HttpGet();
            case POST:
                return new HttpPost();
            case HEAD:
                return new HttpHead();
            case PUT:
                return new HttpPut();
            case PATCH:
                return new HttpPatch();
            case TRACE:
                return new HttpTrace();
            case DELETE:
                return new HttpDelete();
            case OPTIONS:
                return new HttpOptions();
            default:
                throw new UnsupportedOperationException();
        }

    }

    public static HttpUriRequest completedRequest(String url, HttpMethod httpMethod, List<NameValuePair> params, Charset charset) throws URISyntaxException, UnsupportedEncodingException {

        if (HttpMethod.GET == httpMethod) {
            HttpGet httpRequest = new HttpGet();
            URIBuilder uriBuilder = new URIBuilder(url);
            uriBuilder.setCharset(charset);
            if (params != null) {
                uriBuilder.addParameters(params);
            }
            httpRequest.setURI(uriBuilder.build());
            return httpRequest;
        }

        if (HttpMethod.POST == httpMethod) {
            HttpPost httpRequest = new HttpPost();
            httpRequest.setURI(URI.create(url));
            if (params != null) {
                UrlEncodedFormEntity entity = new UrlEncodedFormEntity(params, charset);
                httpRequest.setEntity(entity);
            }
            return httpRequest;
        }

        //ignore
        throw new UnsupportedOperationException();
    }

}
