package dydeve.site.test;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableMBeanExport;

/**
 * Created by dy on 2017/7/21.
 */
@ComponentScan("dydeve.*")
@EnableMBeanExport
@SpringBootApplication
public class Application {

    /*public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }*/

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
