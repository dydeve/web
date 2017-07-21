package com.dydeve.web.config;

import com.dydeve.web.handler.WebObjectMethodArgumentResolver;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.util.List;

/**
 * Created by dy on 2017/7/21.
 */
@Configuration
public class WebMvcConfigurerAdapterConfiguration {

    @Configuration
    @ConditionalOnWebApplication
    protected static class MobApiMvcConfiguration extends WebMvcConfigurerAdapter {

        @Override
        public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
            argumentResolvers.add(new WebObjectMethodArgumentResolver());
        }
    }

}
