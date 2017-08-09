package dydeve.rest.response;

import dydeve.common.util.CloseableUtils;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.CloseableHttpResponse;
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
                return EntityUtils.toString(response.getEntity());//ok close entity
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
                return EntityUtils.toByteArray(response.getEntity());//ok close entity
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
