package dydeve.rest.response;

import com.alibaba.fastjson.JSON;
import dydeve.common.reflect.TypeReference;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.impl.client.AbstractResponseHandler;
import org.apache.http.util.EntityUtils;

import javax.annotation.concurrent.ThreadSafe;
import java.io.IOException;
import java.lang.reflect.Type;

/**
 * @see AbstractResponseHandler
 * Created by dy on 2017/8/9.
 */
@ThreadSafe
public class JsonResponseHandler<T> implements ResponseHandler<T> {

    protected final Type type;

    public JsonResponseHandler(Class<T> clazz) {
        this.type = clazz;
    }

    public JsonResponseHandler(TypeReference<T> typeReference) {
        this.type = typeReference.getType();
    }

    /**
     * close response in closeableHttpClient.execute
     * @param response
     * @return
     * @throws IOException
     */
    @Override
    public T handleResponse(final HttpResponse response)
            throws IOException {
        final StatusLine statusLine = response.getStatusLine();
        final HttpEntity entity = response.getEntity();
        if (statusLine.getStatusCode() / 100 ==2) {
            return handleEntity(entity);
        }
        EntityUtils.consume(entity);
        throw new HttpResponseException(statusLine.getStatusCode(),
                statusLine.getReasonPhrase());

    }

    private T handleEntity(HttpEntity entity) throws IOException {
        if (entity == null) {
            return null;
        }
        //consume stream, and httpclient.execute call response.close
        return JSON.parseObject(EntityUtils.toString(entity), type);
    }

}
