package dydeve.site.web.config;

import dydeve.site.web.handler.JsonHttpMessageConverter;
import dydeve.site.web.handler.RequestResponseJsonMethodProcessor;
import dydeve.site.web.handler.WebObjectMethodArgumentResolver;
import dydeve.site.web.holder.CharsetHolder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;

/**
 * Created by dy on 2017/7/21.
 */
@Configuration
public class WebMvcConfigurerAdapterConfiguration {

    @Configuration
    /**
     * 使用了 @EnableWebMvc 注解后 WebMvcAutoConfiguration 提供的默认配置会失效，必须提供全部配置。
     */
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
            //converters.add(new JsonHttpMessageConverter());
            for (int i = 0; i < converters.size(); i++) {
                if (converters.get(i) instanceof StringHttpMessageConverter) {
                    StringHttpMessageConverter sm = new StringHttpMessageConverter(CharsetHolder.UTF_8);
                    sm.setWriteAcceptCharset(false);
                    converters.set(i, sm);
                    break;
                }
            }
        }

        @Override
        public void addResourceHandlers(ResourceHandlerRegistry registry) {
            registry.addResourceHandler("/static/**")
                    .addResourceLocations("classpath:/static/")
                    .resourceChain(false);
        }

        /*@Override
        public void configureHandlerExceptionResolvers(List<HandlerExceptionResolver> exceptionResolvers) {

            exceptionResolvers.add(new HandlerExceptionResolver() {
                @Override
                public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
                    return null;
                }
            });
            super.configureHandlerExceptionResolvers(exceptionResolvers);
        }*/
    }

}
