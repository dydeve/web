package dydeve.site.test;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableMBeanExport;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Created by dy on 2017/7/21.
 */
@EnableTransactionManagement
@ComponentScan("dydeve.*")
@EnableMBeanExport
@SpringBootApplication
public class Application {

    /*public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }*/

    //@Qualifier("transactionManager")
    @Bean
    public Object testBean(DataSourceTransactionManager platformTransactionManager){
        System.out.println(">>>>>>>>>>" + platformTransactionManager.getClass().getName());
        return new Object();
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
