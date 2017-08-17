package dydeve.rest.client;

import org.apache.http.NameValuePair;
import org.apache.http.client.ResponseHandler;
import org.springframework.http.HttpMethod;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

/**
 * Created by yuduy on 2017/8/17.
 */
public interface HttpClientExecutorCore {

    String rtnString(String url, HttpMethod httpMethod, List<NameValuePair> params) throws IOException, URISyntaxException;

    public byte[] rtnByteArray(String url, HttpMethod httpMethod, List<NameValuePair> params) throws IOException, URISyntaxException;

    <T> T rtnObject(String url, HttpMethod httpMethod, List<NameValuePair> params, ResponseHandler<T> handler) throws IOException, URISyntaxException;

}
