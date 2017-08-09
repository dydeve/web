package dydeve.rest.request;

import org.apache.http.client.methods.*;
import org.springframework.http.HttpMethod;

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


}
