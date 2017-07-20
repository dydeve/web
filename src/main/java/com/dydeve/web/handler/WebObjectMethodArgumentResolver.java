package com.dydeve.web.handler;

import com.dydeve.web.handler.annotation.WebObject;
import org.springframework.core.Conventions;
import org.springframework.core.MethodParameter;
import org.springframework.core.ResolvableType;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.InvalidMediaTypeException;
import org.springframework.http.MediaType;
import org.springframework.http.converter.GenericHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.validation.BindingResult;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.servlet.mvc.method.annotation.AbstractMessageConverterMethodArgumentResolver;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

/**
 * @see AbstractMessageConverterMethodArgumentResolver
 * Created by dy on 2017/7/20.
 */
public class WebObjectMethodArgumentResolver implements HandlerMethodArgumentResolver {

    private HttpMessageConverter<?> converter;

    public WebObjectMethodArgumentResolver(HttpMessageConverter<?> messageConverters) {
        this.converter = messageConverters;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(WebObject.class);
    }

    //todo validate  :  need to learn
    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        Object arg = readWithMessageConverters(webRequest, parameter, parameter.getGenericParameterType());
        return arg;
    }

    /**
     * Throws MethodArgumentNotValidException if validation fails.
     * @throws HttpMessageNotReadableException if {@link RequestBody#required()}
     * is {@code true} and there is no body content or if there is no suitable
     * converter to read the content with.
     */
    /*@Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {

        //httpmessageconverter
        Object arg = readWithMessageConverters(webRequest, parameter, parameter.getGenericParameterType());
        String name = Conventions.getVariableNameForParameter(parameter);
        WebDataBinder binder = binderFactory.createBinder(webRequest, arg, name);
        if (arg != null) {
            //todo validate  :  need to learn
            validateIfApplicable(binder, parameter);
            if (binder.getBindingResult().hasErrors() && isBindExceptionRequired(binder, parameter)) {
                throw new MethodArgumentNotValidException(parameter, binder.getBindingResult());
            }
        }
        mavContainer.addAttribute(BindingResult.MODEL_KEY_PREFIX + name, binder.getBindingResult());
        return arg;
    }*/


    /**
     * Create the method argument value of the expected parameter type by
     * reading from the given request.
     * @param <T> the expected type of the argument value to be created
     * @param webRequest the current request
     * @param methodParam the method argument
     * @param paramType the type of the argument value to be created
     * @return the created method argument value
     * @throws IOException if the reading from the request fails
     * @throws HttpMediaTypeNotSupportedException if no suitable message converter is found
     */
    protected <T> Object readWithMessageConverters(NativeWebRequest webRequest,
                                                   MethodParameter methodParam, Type paramType) throws IOException, HttpMediaTypeNotSupportedException {

        HttpInputMessage inputMessage = createInputMessage(webRequest);
        return readWithMessageConverters(inputMessage, methodParam, paramType);
    }

    /**
     * Create a new {@link HttpInputMessage} from the given {@link NativeWebRequest}.
     * @param webRequest the web request to create an input message from
     * @return the input message
     */
    protected ServletServerHttpRequest createInputMessage(NativeWebRequest webRequest) {
        HttpServletRequest servletRequest = webRequest.getNativeRequest(HttpServletRequest.class);
        return new ServletServerHttpRequest(servletRequest);
    }

    /**
     * Create the method argument value of the expected parameter type by reading
     * from the given HttpInputMessage.
     * @param <T> the expected type of the argument value to be created
     * @param inputMessage the HTTP input message representing the current request
     * @param methodParam the method parameter descriptor
     * @param targetType the type of object to create, not necessarily the same as
     * the method parameter type (e.g. for {@code HttpEntity<String>} method
     * parameter the target type is String)
     * @return the created method argument value
     * @throws IOException if the reading from the request fails
     * @throws HttpMediaTypeNotSupportedException if no suitable message converter is found
     */
    @SuppressWarnings("unchecked")
    protected <T> Object readWithMessageConverters(HttpInputMessage inputMessage,
                                                   MethodParameter methodParam, Type targetType) throws IOException, HttpMediaTypeNotSupportedException {

        MediaType contentType;
        try {
            contentType = inputMessage.getHeaders().getContentType();
        }
        catch (InvalidMediaTypeException ex) {
            contentType = null;
        }

        Class<T> targetClass = (Class<T>)
                ResolvableType.forMethodParameter(methodParam, targetType).resolve(Object.class);

        if (converter.canRead(targetClass, contentType)) {
            return ((HttpMessageConverter<T>) converter).read(targetClass, inputMessage);
        }

        throw new HttpMediaTypeNotSupportedException(contentType, converter.getSupportedMediaTypes());
    }
}
