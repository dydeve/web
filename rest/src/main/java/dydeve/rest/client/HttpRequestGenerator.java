package dydeve.rest.client;

import org.apache.http.client.methods.*;
import org.apache.http.client.utils.URIBuilder;

import java.net.URISyntaxException;

/**
 * i don't use switch case to avoid converted
 * 不用switch case 避免强转
 * Created by yuduy on 2017/8/9.
 */
public class HttpRequestGenerator {

    public static HttpGet generate(HttpMethod.Get unused) {
        return new HttpGet();
    }

    public static HttpPost generate(HttpMethod.Post unused) {
        return new HttpPost();
    }

    public static HttpHead generate(HttpMethod.Head unused) {
        return new HttpHead();
    }

    public static HttpOptions generate(HttpMethod.Options unused) {
        return new HttpOptions();
    }

    public static HttpPut generate(HttpMethod.Put unused) {
        return new HttpPut();
    }

    public static HttpPatch generate(HttpMethod.Patch unused) {
        return new HttpPatch();
    }

    public static HttpDelete generate(HttpMethod.Delete unused) {
        return new HttpDelete();
    }

    public static HttpTrace generate(HttpMethod.Trace unused) {
        return new HttpTrace();
    }

    public static void main(String[] args) throws URISyntaxException {
        System.out.println(new URIBuilder("http://www.duyu.com/a.do?a=2&").addParameter("a", "1").build().toString());
    }

}
