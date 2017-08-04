package dydeve.site.web.config;

import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.EnableMBeanExport;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

/**
 * Created by yuduy on 2017/8/4.
 */
@EnableMBeanExport
@EnableAsync
@EnableScheduling
@EnableAspectJAutoProxy
@EnableWebMvc
@Component
public class Enable {
}
