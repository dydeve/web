package dydeve.rest.client;

import org.apache.http.NameValuePair;
import org.apache.http.client.ResponseHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.concurrent.Future;

/**
 * Created by yuduy on 2017/8/17.
 */
public class HystrixCommandHttpClientExecutor extends HystrixCommandHttpClientExecutorBase implements HystrixCommandHttpClientExecutorCore {

    @Autowired
    public HystrixCommandHttpClientExecutor(HttpClientExecutor httpClientExecutor) {
        super(httpClientExecutor);
    }

    @Override
    public Future<String> queueString(String url, HttpMethod httpMethod, List<NameValuePair> params, HystrixSetting<String> hystrixSetting) {
        return commandString(url, httpMethod, params, hystrixSetting).queue();
    }

    @Override
    public Future<byte[]> queueByteArray(String url, HttpMethod httpMethod, List<NameValuePair> params, HystrixSetting<byte[]> hystrixSetting) {
        return commandByteArray(url, httpMethod, params, hystrixSetting).queue();
    }

    @Override
    public <T> Future<T> queueObject(String url, HttpMethod httpMethod, List<NameValuePair> params, ResponseHandler<T> handler, HystrixSetting<T> hystrixSetting) {
        return commandObject(url, httpMethod, params, handler, hystrixSetting).queue();
    }

    @Override
    public String executeString(String url, HttpMethod httpMethod, List<NameValuePair> params, HystrixSetting<String> hystrixSetting) throws IOException, URISyntaxException {
        return commandString(url, httpMethod, params, hystrixSetting).execute();
    }

    @Override
    public byte[] executeByteArray(String url, HttpMethod httpMethod, List<NameValuePair> params, HystrixSetting<byte[]> hystrixSetting) throws IOException, URISyntaxException {
        return commandByteArray(url, httpMethod, params, hystrixSetting).execute();
    }

    @Override
    public <T> T executeObject(String url, HttpMethod httpMethod, List<NameValuePair> params, ResponseHandler<T> handler, HystrixSetting<T> hystrixSetting) throws IOException, URISyntaxException {
        return commandObject(url, httpMethod, params, handler, hystrixSetting).execute();
    }
}
