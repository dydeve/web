package dydeve.monitor.config;

import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Component;

/**
 * Created by yuduy on 2017/7/29.
 */
@Component
@EnableAspectJAutoProxy(proxyTargetClass = true)
public class AspectjConfig {
}
