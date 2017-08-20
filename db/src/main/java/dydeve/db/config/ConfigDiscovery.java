package dydeve.db.config;

import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;

/**
 * Created by dy on 2017/8/20.
 */
public class ConfigDiscovery implements EnvironmentAware {

    private Environment environment;

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }
}
