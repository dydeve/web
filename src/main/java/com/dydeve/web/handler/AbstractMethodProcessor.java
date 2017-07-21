package com.dydeve.web.handler;

import com.dydeve.web.validate.Validate;
import org.springframework.core.MethodParameter;
import org.springframework.validation.Errors;
import org.springframework.web.bind.WebDataBinder;

/**
 * Created by dy on 2017/7/22.
 */
public abstract class AbstractMethodProcessor {

    /**
     * Validate the request part if applicable.
     * <p>The default implementation checks for {@code @javax.validation.Valid},
     * Spring's {@link org.springframework.validation.annotation.Validated},
     * @param binder the DataBinder to be used
     * @param methodParam the method parameter
     */
    protected void validateIfApplicable(WebDataBinder binder, MethodParameter methodParam) {

        Validate validated = methodParam.getParameterAnnotation(Validate.class);
        if (validated != null) {
            Object hints = validated.value();
            Object[] validationHints = (hints instanceof Object[] ? (Object[]) hints : new Object[] {hints});
            binder.validate(validationHints);
        }

    }

    /**
     * Whether to raise a fatal bind exception on validation errors.
     * @param binder the data binder used to perform data binding
     * @param methodParam the method argument
     * @return {@code true} if the next method argument is not of type {@link Errors}
     * @since 4.1.5
     */
    protected boolean isBindExceptionRequired(WebDataBinder binder, MethodParameter methodParam) {
        int i = methodParam.getParameterIndex();
        Class<?>[] paramTypes = methodParam.getMethod().getParameterTypes();
        boolean hasBindingResult = (paramTypes.length > (i + 1) && Errors.class.isAssignableFrom(paramTypes[i + 1]));
        return !hasBindingResult;
    }

}
