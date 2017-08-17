package dydeve.rest.client;

import com.netflix.hystrix.HystrixCommand;
import org.apache.http.NameValuePair;
import org.apache.http.client.ResponseHandler;
import org.springframework.http.HttpMethod;

import java.util.List;

/**
 * Created by yuduy on 2017/8/17.
 */
public class HystrixCommandHttpClientExecutorBase {

    private final HttpClientExecutor httpClientExecutor;

    public HystrixCommandHttpClientExecutorBase(HttpClientExecutor httpClientExecutor) {
        this.httpClientExecutor = httpClientExecutor;
    }

    public HttpClientExecutor getHttpClientExecutor() {
        return this.httpClientExecutor;
    }

    protected HystrixCommand<String> commandString(String url, HttpMethod httpMethod, List<NameValuePair> params, HystrixSetting<String> hystrixSetting) {
        return new HystrixCommand<String>(hystrixSetting.setter()) {

            @Override
            protected String getFallback() {
                return hystrixSetting.getFallback();
            }

            @Override
            protected String run() throws Exception {
                return httpClientExecutor.rtnString(url, httpMethod, params);
            }
        };
    }

    protected HystrixCommand<byte[]> commandByteArray(String url, HttpMethod httpMethod, List<NameValuePair> params, HystrixSetting<byte[]> hystrixSetting) {
        return new HystrixCommand<byte[]>(hystrixSetting.setter()) {

            @Override
            protected byte[] getFallback() {
                return hystrixSetting.getFallback();
            }

            @Override
            protected byte[] run() throws Exception {
                return httpClientExecutor.rtnByteArray(url, httpMethod, params);
            }
        };
    }

    <T> HystrixCommand<T> commandObject(String url, HttpMethod httpMethod, List<NameValuePair> params, ResponseHandler<T> handler, HystrixSetting<T> hystrixSetting) {
        return new HystrixCommand<T>(hystrixSetting.setter()) {

            @Override
            protected T getFallback() {
                return hystrixSetting.getFallback();
            }

            @Override
            protected T run() throws Exception {
                return httpClientExecutor.rtnObject(url, httpMethod, params, handler);
            }
        };
    }


}
