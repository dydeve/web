package dydeve.rest.client;

import org.apache.http.NameValuePair;
import org.apache.http.client.ResponseHandler;
import org.springframework.http.HttpMethod;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.concurrent.Future;

/**
 * Created by yuduy on 2017/8/17.
 */
public interface HystrixCommandHttpClientExecutorCore {

    Future<String> queueString(String url, HttpMethod httpMethod, List<NameValuePair> params, HystrixSetting<String> hystrixSetting);

    Future<byte[]> queueByteArray(String url, HttpMethod httpMethod, List<NameValuePair> params, HystrixSetting<byte[]> hystrixSetting);

    <T> Future<T> queueObject(String url, HttpMethod httpMethod, List<NameValuePair> params, ResponseHandler<T> handler, HystrixSetting<T> hystrixSetting);


    String executeString(String url, HttpMethod httpMethod, List<NameValuePair> params, HystrixSetting<String> hystrixSetting) throws IOException, URISyntaxException;

    public byte[] executeByteArray(String url, HttpMethod httpMethod, List<NameValuePair> params, HystrixSetting<byte[]> hystrixSetting) throws IOException, URISyntaxException;

    <T> T executeObject(String url, HttpMethod httpMethod, List<NameValuePair> params, ResponseHandler<T> handler, HystrixSetting<T> hystrixSetting) throws IOException, URISyntaxException;


}
