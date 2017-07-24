package dydeve.web.handler;

import com.alibaba.fastjson.JSON;
import com.dydeve.web.handler.annotation.JsonBy;
import com.dydeve.web.handler.annotation.RequestJson;
import com.dydeve.web.handler.annotation.ResponseJson;
import com.dydeve.web.holder.EnvironmentHolder;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.Conventions;
import org.springframework.core.MethodParameter;
import org.springframework.core.ResolvableType;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.InvalidMediaTypeException;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.validation.BindingResult;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.accept.ContentNegotiationManager;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.servlet.mvc.method.annotation.AbstractMessageConverterMethodProcessor;
import org.springframework.web.servlet.mvc.support.DefaultHandlerExceptionResolver;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.io.PushbackInputStream;
import java.lang.reflect.Type;
import java.net.URLDecoder;
import java.util.*;

/**
 * Resolves method arguments annotated with {@code @RequestJson} and handles return
 * values from methods annotated with {@code @ResponseJson} by reading and writing
 * to the body of the request or response with an {@link HttpMessageConverter}.
 *
 * <p>An {@code @RequestJson} method argument is also validated if it is annotated
 * with {@code @org.springframework.validation.annotation.Validated}. In case of validation failure,
 * {@link MethodArgumentNotValidException} is raised and results in an HTTP 400
 * response status code if {@link DefaultHandlerExceptionResolver} is configured.
 *
 * @see org.springframework.web.servlet.mvc.method.annotation.RequestResponseBodyMethodProcessor
 * Created by dy on 2017/7/21.
 */

public class RequestResponseJsonMethodProcessor extends AbstractMessageConverterMethodProcessor implements Validator, InitializingBean {

    int globalJsonBy;

    private HttpMessageConverter<?> httpMessageConverter;

    public RequestResponseJsonMethodProcessor(List<HttpMessageConverter<?>> messageConverters) {
        super(messageConverters);
        httpMessageConverter = messageConverters.get(0);
    }

    public RequestResponseJsonMethodProcessor(List<HttpMessageConverter<?>> messageConverters,
                                              ContentNegotiationManager contentNegotiationManager) {

        super(messageConverters, contentNegotiationManager);
        httpMessageConverter = messageConverters.get(0);
    }

    public RequestResponseJsonMethodProcessor(List<HttpMessageConverter<?>> messageConverters,
                                              ContentNegotiationManager contentNegotiationManager, List<Object> responseBodyAdvice) {

        super(messageConverters, contentNegotiationManager, responseBodyAdvice);
        httpMessageConverter = messageConverters.get(0);
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {

        return parameter.hasParameterAnnotation(RequestJson.class);
    }

    @Override
    public boolean supportsReturnType(MethodParameter returnType) {
        return (AnnotationUtils.findAnnotation(returnType.getContainingClass(), ResponseJson.class) != null ||
                returnType.getMethodAnnotation(ResponseJson.class) != null);
    }

    /**
     * Throws MethodArgumentNotValidException if validation fails.
     * @throws HttpMessageNotReadableException if {@link RequestJson#required()}
     * is {@code true} and there is no body content or if there is no suitable
     * converter to read the content with.
     */
    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {

        Object arg = readFromHttpRequest(webRequest, parameter, parameter.getGenericParameterType());
        String name = Conventions.getVariableNameForParameter(parameter);
        WebDataBinder binder = binderFactory.createBinder(webRequest, arg, name);
        if (arg != null) {
            validateIfApplicable(binder, parameter);
            if (binder.getBindingResult().hasErrors() && isBindExceptionRequired(binder, parameter)) {
                throw new MethodArgumentNotValidException(parameter, binder.getBindingResult());
            }
        }
        mavContainer.addAttribute(BindingResult.MODEL_KEY_PREFIX + name, binder.getBindingResult());
        return arg;
    }

    <T> Object readFromHttpRequest(NativeWebRequest webRequest, MethodParameter parameter,
                                         Type paramType) throws IOException, HttpMediaTypeNotSupportedException {
        RequestJson json = parameter.getParameterAnnotation(RequestJson.class);

        HttpServletRequest httpServletRequest = webRequest.getNativeRequest(HttpServletRequest.class);

        String requestParam = json.value();

        if (requestParam.length() == 0 && "POST".equals(httpServletRequest.getMethod())) {
            return readWithMessageConverters(webRequest, parameter, parameter.getGenericParameterType());
        }

        String jsonString;
        if (requestParam.length() == 0) {
            jsonString = httpServletRequest.getQueryString();
        } else {
            jsonString = httpServletRequest.getParameter(requestParam);
        }

        jsonString = URLDecoder.decode(jsonString, "UTF-8");

        JsonBy jsonBy = parameter.getMethodAnnotation(JsonBy.class);
        if (jsonBy == null) {
            jsonBy = AnnotationUtils.findAnnotation(parameter.getContainingClass(), JsonBy.class);
        }

        int jsonby = jsonBy != null ? jsonBy.value() : globalJsonBy;
        return JSON.parseObject(jsonString, paramType);

    }


    @Override
    protected <T> Object readWithMessageConverters(NativeWebRequest webRequest, MethodParameter methodParam,
                                                   Type paramType) throws IOException, HttpMediaTypeNotSupportedException {

        HttpServletRequest servletRequest = webRequest.getNativeRequest(HttpServletRequest.class);
        HttpInputMessage inputMessage = new ServletServerHttpRequest(servletRequest);

        InputStream inputStream = inputMessage.getBody();
        if (inputStream == null) {
            return handleEmptyBody(methodParam);
        }
        else if (inputStream.markSupported()) {
            inputStream.mark(1);
            if (inputStream.read() == -1) {
                return handleEmptyBody(methodParam);
            }
            inputStream.reset();
        }
        else {
            final PushbackInputStream pushbackInputStream = new PushbackInputStream(inputStream);
            int b = pushbackInputStream.read();
            if (b == -1) {
                return handleEmptyBody(methodParam);
            }
            else {
                pushbackInputStream.unread(b);
            }
            inputMessage = new ServletServerHttpRequest(servletRequest) {
                @Override
                public InputStream getBody() {
                    // Form POST should not get here
                    return pushbackInputStream;
                }
            };
        }

        return this.readWithMessageConverters(inputMessage, methodParam, paramType);
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
            throw new HttpMediaTypeNotSupportedException(ex.getMessage());
        }

        Class<T> targetClass = (Class<T>)
                ResolvableType.forMethodParameter(methodParam, targetType).resolve(Object.class);

        if (httpMessageConverter.canRead(targetClass, contentType)) {
            if (logger.isDebugEnabled()) {
                logger.debug("Reading [" + targetClass.getName() + "] as \"" +
                        contentType + "\" using [" + httpMessageConverter + "]");
            }
            return ((HttpMessageConverter<T>) httpMessageConverter).read(targetClass, inputMessage);
        }

        throw new HttpMediaTypeNotSupportedException(contentType, this.allSupportedMediaTypes);
    }

    private Object handleEmptyBody(MethodParameter param) {
        if (param.getParameterAnnotation(RequestJson.class).required()) {
            throw new HttpMessageNotReadableException("Required request body content is missing: " + param);
        }
        return null;
    }

    @Override
    public void handleReturnValue(Object returnValue, MethodParameter returnType,
                                  ModelAndViewContainer mavContainer, NativeWebRequest webRequest)
            throws IOException, HttpMediaTypeNotAcceptableException {

        mavContainer.setRequestHandled(true);

        // Try even with null return value. ResponseBodyAdvice could get involved.
        writeWithMessageConverters(returnValue, returnType, webRequest);
    }

    @Override
    public void validateIfApplicable(WebDataBinder binder, MethodParameter methodParam) {
        Validator.super.validateIfApplicable(binder, methodParam);
    }

    @Override
    public boolean isBindExceptionRequired(WebDataBinder binder, MethodParameter methodParam) {
        return Validator.super.isBindExceptionRequired(binder, methodParam);
    }


    // TODO: 2017/7/22 problem
    @Override
    public void afterPropertiesSet() throws Exception {
        String jsonBy = EnvironmentHolder.getEnvironment().getProperty(JsonBy.WEB_JSONBY);
        switch (jsonBy) {
            case "fastJson":
                globalJsonBy = 0;
                break;
            case "gson":
                globalJsonBy = 1;
                break;
            case "jackson":
                globalJsonBy = 2;
                break;
            default:
                throw new IllegalStateException("no " + jsonBy + " defination");
        }
        System.out.println("!!!!!  jsonBy" + "  " + globalJsonBy);
    }
}
