package dydeve.site.web.config;

import dydeve.site.web.controller.GlobalErrorController;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.SearchStrategy;
import org.springframework.boot.autoconfigure.web.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;

/**
 * @see ErrorMvcAutoConfiguration
 * Created by yuduy on 2017/8/4.
 */
@Configuration
@AutoConfigureBefore(ErrorMvcAutoConfiguration.class)
public class GlobalErrorConfiguration /*implements Ordered*/ {


    @Bean
    @ConditionalOnMissingBean(value = ErrorAttributes.class, search = SearchStrategy.CURRENT)
    public DefaultErrorAttributes errorAttributes() {
        return new DefaultErrorAttributes();
    }

    @Bean
    @ConditionalOnMissingBean(value = ErrorController.class, search = SearchStrategy.CURRENT)
    public GlobalErrorController globalErrorConfiguration(ErrorAttributes errorAttributes) {
        return new GlobalErrorController(errorAttributes);
    }

    /*@Override
    public int getOrder() {
        return -1;
    }*/
}
