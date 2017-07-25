package dydeve.site.web.handler;

import com.alibaba.fastjson.JSON;
import dydeve.site.web.holder.CharsetHolder;
import dydeve.site.web.vo.ApiResponse;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.AbstractHttpMessageConverter;
import org.springframework.http.converter.GenericHttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by dy on 2017/7/22.
 */
public class JsonHttpMessageConverter extends AbstractHttpMessageConverter<Object>
        implements GenericHttpMessageConverter<Object> {

    public JsonHttpMessageConverter() {
        super(new MediaType("application", "json", CharsetHolder.UTF_8),
                new MediaType("application", "*+json", CharsetHolder.UTF_8));
    }

    @Override
    protected boolean supports(Class<?> clazz) {
        return true;
    }

    @Override
    public void setSupportedMediaTypes(List<MediaType> supportedMediaTypes) {
        super.setSupportedMediaTypes(supportedMediaTypes);
    }

    @Override
    protected Object readInternal(Class<?> clazz, HttpInputMessage inputMessage) throws IOException, HttpMessageNotReadableException {
        return JSON.parseObject(inputMessage.getBody(), clazz);
    }

    @Override
    protected void writeInternal(Object o, HttpOutputMessage outputMessage) throws IOException, HttpMessageNotWritableException {

        ApiResponse response;
        if (o == null) {//todo:实际用不到
            response = ApiResponse.EMPTY_RESPONSE;
        } else if (o instanceof ApiResponse) {
            response = (ApiResponse)o;
        } else {
            response = ApiResponse.success(o);
        }


        ByteArrayOutputStream outnew = new ByteArrayOutputStream();
        int length = JSON.writeJSONString(outnew, response);
        outputMessage.getHeaders().setContentLength(length);
        outnew.writeTo(outputMessage.getBody());

        outnew.close();
    }

    @Override
    public boolean canRead(Type type, Class<?> contextClass, MediaType mediaType) {
        return super.canRead(contextClass, mediaType);
    }

    @Override
    public Object read(Type type, Class<?> contextClass, HttpInputMessage inputMessage) throws IOException, HttpMessageNotReadableException {
        return JSON.parseObject(inputMessage.getBody(), CharsetHolder.UTF_8, type);
    }
}
