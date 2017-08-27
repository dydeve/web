package dydeve.db;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * https://github.com/handosme/JMay
 * Created by yuduy on 2017/8/18.
 */
@EnableAutoConfiguration
@EnableTransactionManagement(order = 10) //开启事务，并设置order值，默认是Integer的最大值
@SpringBootApplication
public class Test extends SpringBootServletInitializer implements EmbeddedServletContainerCustomizer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(Test.class);
    }


    public static void main(String[] args) throws Exception {
        SpringApplication.run(Test.class, args);
    }

    public void customize(ConfigurableEmbeddedServletContainer configurableEmbeddedServletContainer) {
        //	configurableEmbeddedServletContainer.setPort(9090);
    }
}