package dydeve.site.web.handler;

import dydeve.site.web.exception.InvalidParamException;
import dydeve.site.web.handler.annotation.WebObject;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.core.Conventions;
import org.springframework.core.MethodParameter;
import org.springframework.util.ClassUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.annotation.RequestParamMapMethodArgumentResolver;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.servlet.mvc.method.annotation.AbstractMessageConverterMethodArgumentResolver;
import org.springframework.web.servlet.mvc.method.annotation.RequestResponseBodyMethodProcessor;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.Map;

/**
 * @see AbstractMessageConverterMethodArgumentResolver
 * @see RequestParamMapMethodArgumentResolver
 * @see RequestResponseBodyMethodProcessor
 * Created by dy on 2017/7/20.
 */
public class WebObjectMethodArgumentResolver implements HandlerMethodArgumentResolver, Validator {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(WebObject.class);
    }

    //todo validate  :  need to learn
    //fixme urlencode
    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {

        Map<String, String[]> parameterMap = webRequest.getParameterMap();
        if (parameterMap == null || parameterMap.isEmpty()) {
            if (parameter.getParameterAnnotation(WebObject.class).required()) {
                throw new InvalidParamException("request param can't be null", -1);
            }
            return null;
        }
        //不支持map做想象中的入参 或者把convert放前面
        /*Class<?> clazz = parameter.getParameterType();
        if (Map.class.isAssignableFrom(clazz)) {
            ParameterizedType mapGenericType = (ParameterizedType) parameter.getGenericParameterType();
            if (String.class.equals(mapGenericType.getActualTypeArguments()[0])
                    && String[].class.equals(mapGenericType.getActualTypeArguments()[1])) {
                return parameterMap;
            } else {
                throw new UnsupportedOperationException("just support map in form of map<String, String[]>");
            }
        }*/

        //convert to map
        MultiValueMap<String, String> multiValueMap = new LinkedMultiValueMap<>(parameterMap.size());
        for (Map.Entry<String, String[]> entry : parameterMap.entrySet()) {
            for (String value : entry.getValue()) {
                multiValueMap.add(entry.getKey(), value);
            }
        }


        /*if (MultiValueMap.class.isAssignableFrom(paramType)) {
            return multiValueMap;
        }*/

        //convert to object
        Object param = parameter.getParameterType().newInstance();//必须有无参构造函数
        /*for (Field field : clazz.getDeclaredFields()) {array list collection map
            String[] values = parameterMap.get(field.getName());
        }*/
        BeanUtils.populate(param, multiValueMap);

        //validate if applicable
        String name = Conventions.getVariableNameForParameter(parameter);
        WebDataBinder binder = binderFactory.createBinder(webRequest, param, name);
        validateIfApplicable(binder, parameter);

        if (binder.getBindingResult().hasErrors() && isBindExceptionRequired(binder, parameter)) {
            throw new MethodArgumentNotValidException(parameter, binder.getBindingResult());
        }
        mavContainer.addAttribute(BindingResult.MODEL_KEY_PREFIX + name, binder.getBindingResult());
        return param;

    }



}
