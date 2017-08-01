package dydeve.site.web.config;

import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.web.ErrorMvcAutoConfiguration;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

/**
 * Created by dy on 2017/7/22.
 */
@Configuration
@AutoConfigureBefore(ErrorMvcAutoConfiguration.class)
public class RequestResponseJsonCinfiguration {

    /*@Bean
    public RequestResponseJsonMethodProcessor requestResponseJsonMethodProcessor(EnvironmentHolder environmentHolder) {
        RequestResponseJsonMethodProcessor processor = new RequestResponseJsonMethodProcessor(Arrays.asList(new JsonHttpMessageConverter()))
    }*/

}
