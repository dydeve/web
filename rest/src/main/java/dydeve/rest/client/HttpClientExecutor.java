package dydeve.rest.client;

import dydeve.rest.response.JsonResponseHandler;
import org.apache.http.NameValuePair;
import org.springframework.http.HttpMethod;

import java.beans.IntrospectionException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;

/**
 * Created by yuduy on 2017/8/11.
 */
public interface HttpClientExecutor {

    String rtnString(String url, HttpMethod httpMethod, Object param) throws IllegalAccessException, IntrospectionException, InvocationTargetException, IOException, URISyntaxException;

    String rtnString(String url, HttpMethod httpMethod, Map<String, Object> params) throws IOException, URISyntaxException;

    String rtnString(String url, HttpMethod httpMethod, List<NameValuePair> params) throws IOException, URISyntaxException;

    byte[] rtnByteArray(String url, HttpMethod httpMethod, Object param) throws IllegalAccessException, IntrospectionException, InvocationTargetException, IOException, URISyntaxException;

    byte[] rtnByteArray(String url, HttpMethod httpMethod, Map<String, Object> params) throws IllegalAccessException, IntrospectionException, InvocationTargetException, IOException, URISyntaxException;

    byte[] rtnByteArray(String url, HttpMethod httpMethod, List<NameValuePair> params) throws IOException, URISyntaxException;

    <T> T rtnObject(String url, HttpMethod httpMethod, Object param, JsonResponseHandler<T> handler) throws IOException, URISyntaxException, IllegalAccessException, IntrospectionException, InvocationTargetException;

    <T> T rtnObject(String url, HttpMethod httpMethod, Map<String, Object> params, JsonResponseHandler<T> handler) throws IOException, URISyntaxException;

    <T> T rtnObject(String url, HttpMethod httpMethod, List<NameValuePair> params, JsonResponseHandler<T> handler) throws IOException, URISyntaxException;


    /*==========HttpMethod.GET===========*/

    String getString(String url, Object param) throws IllegalAccessException, IntrospectionException, InvocationTargetException, IOException, URISyntaxException;

    String getString(String url, Map<String, Object> params) throws IOException, URISyntaxException;

    String getString(String url, List<NameValuePair> params) throws IOException, URISyntaxException;

    byte[] getByteArray(String url, Object param) throws IllegalAccessException, IntrospectionException, InvocationTargetException, IOException, URISyntaxException;

    byte[] getByteArray(String url, Map<String, Object> params) throws IllegalAccessException, IntrospectionException, InvocationTargetException, IOException, URISyntaxException;

    byte[] getByteArray(String url, List<NameValuePair> params) throws IOException, URISyntaxException;

    <T> T getObject(String url, Object param, JsonResponseHandler<T> handler) throws IOException, URISyntaxException, IllegalAccessException, IntrospectionException, InvocationTargetException;

    <T> T getObject(String url, Map<String, Object> params, JsonResponseHandler<T> handler) throws IOException, URISyntaxException;

    <T> T getObject(String url, List<NameValuePair> params, JsonResponseHandler<T> handler) throws IOException, URISyntaxException;

}
