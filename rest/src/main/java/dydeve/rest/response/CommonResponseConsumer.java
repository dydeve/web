package dydeve.rest.response;

import dydeve.common.util.CloseableUtils;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.AbstractResponseHandler;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Created by dy on 2017/8/9.
 */
public class CommonResponseConsumer {

    private static final Logger log = LoggerFactory.getLogger(CommonResponseConsumer.class);

    private CommonResponseConsumer() {
        throw new UnsupportedOperationException();
    }

    /**
     * @see CloseableHttpClient#execute(HttpHost, HttpRequest, ResponseHandler, HttpContext)
     * it has close stream or response
     * @param httpClient
     * @param httpRequest
     * @param handler
     * @param <T>
     * @return
     * @throws IOException
     */
    public static <T> T returnObject(CloseableHttpClient httpClient, HttpUriRequest httpRequest, ResponseHandler<T> handler) throws IOException {
        return httpClient.execute(httpRequest, handler);
    }

    public static String returnString(CloseableHttpClient httpClient, HttpUriRequest httpRequest) throws IOException {
        return CommonResponseConsumer.response2String(httpClient.execute(httpRequest));
    }

    public static byte[] returnByteArray(CloseableHttpClient httpClient, HttpUriRequest httpRequest) throws IOException {
        return CommonResponseConsumer.response2ByteArray(httpClient.execute(httpRequest));
    }

    /**
     * @see CloseableHttpClient#execute(HttpHost, HttpRequest, ResponseHandler, HttpContext)
     * @see AbstractResponseHandler#handleResponse(HttpResponse)
     * @param response
     * @return
     */
    public static String response2String(CloseableHttpResponse response) throws IOException {
        if (response == null) {
            return null;
        }

        try {
            if (response.getStatusLine().getStatusCode() / 100 == 2) {
                try {
                    return EntityUtils.toString(response.getEntity());//ok close entity
                } finally {
                    EntityUtils.consume(response.getEntity());
                }

            }

            EntityUtils.consume(response.getEntity());
            throw new HttpResponseException(response.getStatusLine().getStatusCode(),
                    response.getStatusLine().getReasonPhrase());
        } /*catch (Exception e) {

            log.error("Exception happens when resolveResponse to string", e);
            throw e;
        } */finally {
            //The connection will not be reused, but all level resources held by it will be correctly deallocated.
            CloseableUtils.close(response);
        }
    }

    public static byte[] response2ByteArray(CloseableHttpResponse response) throws IOException {
        if (response == null) {
            return null;
        }

        try {
            if (response.getStatusLine().getStatusCode() / 100 == 2) {
                try {
                    return EntityUtils.toByteArray(response.getEntity());//ok close entity
                } finally {
                    EntityUtils.consume(response.getEntity());
                }

            }
            EntityUtils.consume(response.getEntity());
            throw new HttpResponseException(response.getStatusLine().getStatusCode(),
                    response.getStatusLine().getReasonPhrase());
        } /*catch (Exception e) {

            log.error("Exception happens when resolveResponse to string", e);
            throw e;
        } */finally {
            //The connection will not be reused, but all level resources held by it will be correctly deallocated.
            CloseableUtils.close(response);
        }
    }

}
