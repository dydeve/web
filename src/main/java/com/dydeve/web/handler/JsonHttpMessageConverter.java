package com.dydeve.web.handler;

import com.alibaba.fastjson.JSON;
import com.dydeve.web.holder.CharsetHolder;
import com.dydeve.web.vo.ApiResponse;
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

/**
 * Created by dy on 2017/7/22.
 */
public class JsonHttpMessageConverter extends AbstractHttpMessageConverter<Object>
        implements GenericHttpMessageConverter<Object> {
    @Override
    protected boolean supports(Class<?> clazz) {
        return true;
    }

    @Override
    protected Object readInternal(Class<?> clazz, HttpInputMessage inputMessage) throws IOException, HttpMessageNotReadableException {
        return null;
    }

    @Override
    protected void writeInternal(Object o, HttpOutputMessage outputMessage) throws IOException, HttpMessageNotWritableException {

        ApiResponse response;
        if (o == null) {
            response = ApiResponse.EMPTY_RESPONSE;
        }

        if (o instanceof ApiResponse) {
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
        return super.canWrite(contextClass, mediaType);
    }

    @Override
    public Object read(Type type, Class<?> contextClass, HttpInputMessage inputMessage) throws IOException, HttpMessageNotReadableException {
        return JSON.parseObject(inputMessage.getBody(), CharsetHolder.UTF_8, type);
    }
}
