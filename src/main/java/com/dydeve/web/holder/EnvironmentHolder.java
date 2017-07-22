package com.dydeve.web.holder;

import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

/**
 * spring environment holder
 * Created by dy on 2017/7/22.
 */
public class EnvironmentHolder implements EnvironmentAware {


    private static Environment environment;

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    public static Environment getEnvironment() {
        return environment;
    }
}
