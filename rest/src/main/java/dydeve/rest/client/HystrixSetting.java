package dydeve.rest.client;

import com.netflix.hystrix.HystrixCommand;

/**
 * Created by yuduy on 2017/8/17.
 */
public class HystrixSetting<T> {

    private final HystrixCommand.Setter setter;

    public HystrixSetting(HystrixCommand.Setter setter) {
        this.setter = setter;
    }

    HystrixCommand.Setter setter() {
        return setter;
    }

    public T getFallback() {
        throw new UnsupportedOperationException("No fallback available.");
    }

}
