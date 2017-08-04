package dydeve.site.web.config;

import dydeve.site.web.controller.GlobalErrorController;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.SearchStrategy;
import org.springframework.boot.autoconfigure.web.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @see ErrorMvcAutoConfiguration
 * Created by yuduy on 2017/8/4.
 */
@AutoConfigureBefore(ErrorMvcAutoConfiguration.class)
@Configuration
public class GlobalErrorConfiguration {


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

}
