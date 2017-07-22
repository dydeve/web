package com.dydeve.web.config;

import com.dydeve.web.handler.JsonHttpMessageConverter;
import com.dydeve.web.handler.RequestResponseJsonMethodProcessor;
import com.dydeve.web.handler.WebObjectMethodArgumentResolver;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.util.Arrays;
import java.util.List;

/**
 * Created by dy on 2017/7/21.
 */
@Configuration
public class WebMvcConfigurerAdapterConfiguration {

    @Configuration
    @ConditionalOnWebApplication
    protected static class MvcConfiguration extends WebMvcConfigurerAdapter {

        RequestResponseJsonMethodProcessor requestResponseJsonMethodProcessor = new RequestResponseJsonMethodProcessor(
                Arrays.asList(
                        new JsonHttpMessageConverter()
                )
        );

        @Override
        public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
            argumentResolvers.add(new WebObjectMethodArgumentResolver());
            argumentResolvers.add(requestResponseJsonMethodProcessor);
        }

        @Override
        public void addReturnValueHandlers(List<HandlerMethodReturnValueHandler> returnValueHandlers) {
            returnValueHandlers.add(requestResponseJsonMethodProcessor);
        }

        @Override
        public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
            converters.add(new JsonHttpMessageConverter());
        }
    }

}
