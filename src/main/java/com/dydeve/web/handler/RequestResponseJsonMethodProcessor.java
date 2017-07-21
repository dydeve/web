package com.dydeve.web.handler;

import com.dydeve.web.handler.annotation.RequestJson;
import com.dydeve.web.handler.annotation.ResponseJson;
import org.springframework.core.Conventions;
import org.springframework.core.MethodParameter;
import org.springframework.core.ResolvableType;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.InvalidMediaTypeException;
import org.springframework.http.MediaType;
import org.springframework.http.converter.GenericHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.validation.BindingResult;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.accept.ContentNegotiationManager;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.servlet.mvc.method.annotation.AbstractMessageConverterMethodProcessor;
import org.springframework.web.servlet.mvc.support.DefaultHandlerExceptionResolver;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.io.PushbackInputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

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

public class RequestResponseJsonMethodProcessor extends AbstractMethodProcessor implements HandlerMethodArgumentResolver, HandlerMethodReturnValueHandler {

    private HttpMessageConverter<?> httpMessageConverter;

    public RequestResponseJsonMethodProcessor(HttpMessageConverter<?> messageConverter) {
        this.httpMessageConverter = messageConverter;
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

        Object arg = readWithMessageConverters(webRequest, parameter, parameter.getGenericParameterType());
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

        return readWithMessageConverters(inputMessage, methodParam, paramType);
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

        Class<?> contextClass = methodParam.getContainingClass();
        Class<T> targetClass = (Class<T>)
                ResolvableType.forMethodParameter(methodParam, targetType).resolve(Object.class);

        if (httpMessageConverter.canRead(targetClass, contentType)) {
            return ((HttpMessageConverter<T>) httpMessageConverter).read(targetClass, inputMessage);
        }

        throw new HttpMediaTypeNotSupportedException(contentType, null);
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

    /**
     * Writes the given return value to the given web request. Delegates to
     * {@link #writeWithMessageConverters(Object, MethodParameter, ServletServerHttpRequest, ServletServerHttpResponse)}
     */
    protected <T> void writeWithMessageConverters(T returnValue, MethodParameter returnType, NativeWebRequest webRequest)
            throws IOException, HttpMediaTypeNotAcceptableException {

        ServletServerHttpRequest inputMessage = createInputMessage(webRequest);
        ServletServerHttpResponse outputMessage = createOutputMessage(webRequest);
        writeWithMessageConverters(returnValue, returnType, inputMessage, outputMessage);
    }

    /**
     * Writes the given return type to the given output message.
     * @param returnValue the value to write to the output message
     * @param returnType the type of the value
     * @param inputMessage the input messages. Used to inspect the {@code Accept} header.
     * @param outputMessage the output message to write to
     * @throws IOException thrown in case of I/O errors
     * @throws HttpMediaTypeNotAcceptableException thrown when the conditions indicated by {@code Accept} header on
     * the request cannot be met by the message converters
     */
    @SuppressWarnings("unchecked")
    protected <T> void writeWithMessageConverters(T returnValue, MethodParameter returnType,
                                                  ServletServerHttpRequest inputMessage, ServletServerHttpResponse outputMessage)
            throws IOException, HttpMediaTypeNotAcceptableException {

        Class<?> returnValueClass = getReturnValueType(returnValue, returnType);
        HttpServletRequest servletRequest = inputMessage.getServletRequest();
        List<MediaType> requestedMediaTypes = getAcceptableMediaTypes(servletRequest);
        List<MediaType> producibleMediaTypes = getProducibleMediaTypes(servletRequest, returnValueClass);

        Set<MediaType> compatibleMediaTypes = new LinkedHashSet<MediaType>();
        for (MediaType requestedType : requestedMediaTypes) {
            for (MediaType producibleType : producibleMediaTypes) {
                if (requestedType.isCompatibleWith(producibleType)) {
                    compatibleMediaTypes.add(getMostSpecificMediaType(requestedType, producibleType));
                }
            }
        }
        if (compatibleMediaTypes.isEmpty()) {
            if (returnValue != null) {
                throw new HttpMediaTypeNotAcceptableException(producibleMediaTypes);
            }
            return;
        }

        List<MediaType> mediaTypes = new ArrayList<MediaType>(compatibleMediaTypes);
        MediaType.sortBySpecificityAndQuality(mediaTypes);

        MediaType selectedMediaType = null;
        for (MediaType mediaType : mediaTypes) {
            if (mediaType.isConcrete()) {
                selectedMediaType = mediaType;
                break;
            }
            else if (mediaType.equals(MediaType.ALL) || mediaType.equals(MEDIA_TYPE_APPLICATION)) {
                selectedMediaType = MediaType.APPLICATION_OCTET_STREAM;
                break;
            }
        }

        if (selectedMediaType != null) {
            selectedMediaType = selectedMediaType.removeQualityValue();
            for (HttpMessageConverter<?> messageConverter : this.messageConverters) {
                if (messageConverter.canWrite(returnValueClass, selectedMediaType)) {
                    returnValue = this.adviceChain.invoke(returnValue, returnType, selectedMediaType,
                            (Class<HttpMessageConverter<?>>) messageConverter.getClass(), inputMessage, outputMessage);
                    if (returnValue != null) {
                        ((HttpMessageConverter<T>) messageConverter).write(returnValue, selectedMediaType, outputMessage);
                        if (logger.isDebugEnabled()) {
                            logger.debug("Written [" + returnValue + "] as \"" + selectedMediaType + "\" using [" +
                                    messageConverter + "]");
                        }
                    }
                    return;
                }
            }
        }

        if (returnValue != null) {
            throw new HttpMediaTypeNotAcceptableException(this.allSupportedMediaTypes);
        }
    }

}
