package dydeve.rest.config;

import org.apache.http.*;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.config.*;
import org.apache.http.conn.DnsResolver;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.conn.HttpConnectionFactory;
import org.apache.http.conn.ManagedHttpClientConnection;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.DefaultConnectionReuseStrategy;
import org.apache.http.impl.client.*;
import org.apache.http.impl.conn.ManagedHttpClientConnectionFactory;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.impl.conn.SystemDefaultDnsResolver;
import org.apache.http.impl.io.DefaultHttpRequestWriterFactory;
import org.apache.http.impl.io.DefaultHttpResponseParserFactory;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.nio.charset.CodingErrorAction;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Created by yuduy on 2017/8/8.
 */
/**
 * 使用consume 或 toString消费响应体。
 *     不推荐 {@link HttpEntity#getContent()#close}, 可能导致连接不释放。
 *     不推荐{@link CloseableHttpResponse#close()},  直接关闭Socket, 导致长连接不能复用
 *
 *
 * 开启长连接时  才是真正的连接池，如果是短连接，只是作为一个信号量限制总请求数，连接没有实现复用
 * jvm停止或重启后，关闭连接池，释放资源
 * httpClient线程安全，不需要每次使用创建一个
 * 如果连接池配置的较大，可以创建多个httpClient实例，而不是使用一个实例
 * 使用连接池时，尽快消费响应体并释放连接到连接池，不要保持太久
 */
@Configuration
public class HttpClientConfig {

    private static final Logger log = LoggerFactory.getLogger(HttpClientConfig.class);

    @Bean(name = "defaultHttpClientConnectionManager")//配置可以更灵活
    public HttpClientConnectionManager poolingHttpClientConnectionManager() {
        //注册访问协议相关工厂
        Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
                .register("http", PlainConnectionSocketFactory.INSTANCE)
                .register("https", SSLConnectionSocketFactory.getSystemSocketFactory())
                .build();
        //httpConnection工厂 :  配置写请求/解析响应处理器
        HttpConnectionFactory<HttpRoute, ManagedHttpClientConnection> connectionFactory = new ManagedHttpClientConnectionFactory(
                DefaultHttpRequestWriterFactory.INSTANCE,
                DefaultHttpResponseParserFactory.INSTANCE
        );
        //DNS解析器
        DnsResolver dnsResolver = SystemDefaultDnsResolver.INSTANCE;
        //创建池化连接管理器
        PoolingHttpClientConnectionManager manager = new PoolingHttpClientConnectionManager(socketFactoryRegistry, connectionFactory, dnsResolver);

        //默认为socket配置
        SocketConfig defaultSocketConfig = SocketConfig.custom().setTcpNoDelay(true).build();

        // Create message constraints
        MessageConstraints messageConstraints = MessageConstraints.custom()
                .setMaxHeaderCount(200)
                .setMaxLineLength(2000)
                .build();
        // Create connection configuration
        ConnectionConfig connectionConfig = ConnectionConfig.custom()
                .setMalformedInputAction(CodingErrorAction.REPORT)
                .setUnmappableInputAction(CodingErrorAction.REPORT)
                .setCharset(Consts.UTF_8)
                .setMessageConstraints(messageConstraints)
                .build();

        manager.setDefaultSocketConfig(defaultSocketConfig);
        manager.setDefaultConnectionConfig(connectionConfig);

        manager.setMaxTotal(300);//设置整个连接池的最大连接数
        /**
         * 每个路由的默认最大连接，
         * 每个路由实际最大连接数默认为DefaultMaxPerRoute控制，
         * MaxTotal控制整个池子的最大数，设置过小无法支持大并发(ConnectionPoolTimeoutException: Timeout waiting for connection from pool),
         * 路由是对maxTotal的细分
         */
        manager.setDefaultMaxPerRoute(200);//每个路由最大连接数
        manager.setMaxPerRoute(new HttpRoute(new HttpHost("jd.com", 80)), 100);
        //从连接池获取连接时，连接不活跃多长时间后需要进行一次验证，默认2s
        //This check helps detect connections that have become stale (half-closed) while kept inactive in the pool.
        manager.setValidateAfterInactivity(5 * 1000);

        return manager;
    }

    @Bean(destroyMethod = "close")
    public CloseableHttpClient closeableHttpClient(@Qualifier("defaultHttpClientConnectionManager") HttpClientConnectionManager manager) {
        //默认请求配置
        RequestConfig defaultRequestConfig = RequestConfig.custom()
                .setConnectTimeout(2 * 1000)//连接超时时间 2s
                .setSocketTimeout(5 * 1000)//等待数据超时时间 5s
                .setConnectionRequestTimeout(2000)//从连接池获取连接的等待时间
                .build();

        //创建httpClient
        CloseableHttpClient httpClient = HttpClients.custom()
                .setConnectionManager(manager)//配置httpClient使用的连接池
                .setConnectionManagerShared(false)//连接池不是共享模式(配置此连接池能否在多个httpClient之间共享，默认false; 如果是共享模式，如IdleConnectionEvictor不能每个client一个，只需定义一个即可)
                .evictIdleConnections(60, TimeUnit.SECONDS)//定期回收空闲连接
                .evictExpiredConnections()//定期回收过期连接
                //通过配置后台线程定期释放过期连接 与 空闲连接。如果连接池共享，多个httpClient共用一个池，则这连个配置无效
                //通过一把大锁 锁住连接池 进行遍历。建议只启用closeExpiredConnections , 这需要服务生产者包含超时时间"Keep-Alive: timeout=time"
                .setConnectionTimeToLive(60, TimeUnit.SECONDS)//连接存活时间，如果不设置，则根据长连接信息决定
                .setDefaultRequestConfig(defaultRequestConfig)//默认请求配置
                .setConnectionReuseStrategy(DefaultConnectionReuseStrategy.INSTANCE)//连接重用策略，即是否能keepAlive
                .setKeepAliveStrategy(DefaultConnectionKeepAliveStrategy.INSTANCE)//长连接配置，即获取长连接生产多长时间
                .setRetryHandler(new DefaultHttpRequestRetryHandler(0, false))//设置重试次数，默认3次; 当前禁用，可根据需要开启
                .build();

        /*//JVM停止或重启时，关闭连接池释放掉连接(跟数据库连接池类似)
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                try {
                    System.err.println("close it");
                    httpClient.close();
                } catch (IOException e) {
                    log.error("close httpClient error.", e);
                }
            }
        });*/


        return httpClient;
    }


    @Bean(destroyMethod = "close")
    public FutureRequestExecutionService futureRequestExecutionService(CloseableHttpClient httpClient) {
        ExecutorService executorService = Executors.newFixedThreadPool(5);
        FutureRequestExecutionService futureRequestExecutionService =
                new FutureRequestExecutionService(httpClient, executorService);
        return futureRequestExecutionService;
    }

    /*public static void consume() throws IOException {
        CloseableHttpResponse response = null;
        try {
            HttpGet get = new HttpGet("http://httpbin.org/get");
            response = getHttpClient().execute(get);
            if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
                EntityUtils.consume(response.getEntity());
                //error
            } else {

                System.out.println("----------------------------------------");
                System.out.println(response.getStatusLine());
                System.out.println("----------------------------------------");
                System.out.println(EntityUtils.toString(response.getEntity()));
                System.out.println("----------------------------------------");

                *//*String result = EntityUtils.toString(response.getEntity());
                System.out.println(result);*//*
            }
        } catch (Exception e) {
            log.error("haha", e);
            if (response != null) {
                response.close();
                try {
                    EntityUtils.consume(response.getEntity());
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }

    private final class OkidokiHandler implements ResponseHandler<Boolean> {
    public Boolean handleResponse(
            final HttpResponse response) throws ClientProtocolException, IOException {
        return response.getStatusLine().getStatusCode() == 200;
    }
}

HttpRequestFutureTask<Boolean> task = futureRequestExecutionService.execute(
    new HttpGet("http://www.google.com"), HttpClientContext.create(),
    new OkidokiHandler());
// blocks until the request complete and then returns true if you can connect to Google
boolean ok=task.get();

task.cancel(true)
task.get() // throws an Exception

private final class MyCallback implements FutureCallback<Boolean> {

    public void failed(final Exception ex) {
        // do something
    }

    public void completed(final Boolean result) {
        // do something
    }

    public void cancelled() {
        // do something
    }
}

HttpRequestFutureTask<Boolean> task = futureRequestExecutionService.execute(
    new HttpGet("http://www.google.com"), HttpClientContext.create(),
    new OkidokiHandler(), new MyCallback());

    task.scheduledTime() // returns the timestamp the task was scheduled
task.startedTime() // returns the timestamp when the task was started
task.endedTime() // returns the timestamp when the task was done executing
task.requestDuration // returns the duration of the http request
task.taskDuration // returns the duration of the task from the moment it was scheduled

FutureRequestExecutionMetrics metrics = futureRequestExecutionService.metrics()
metrics.getActiveConnectionCount() // currently active connections
metrics.getScheduledConnectionCount(); // currently scheduled connections
metrics.getSuccessfulConnectionCount(); // total number of successful requests
metrics.getSuccessfulConnectionAverageDuration(); // average request duration
metrics.getFailedConnectionCount(); // total number of failed tasks
metrics.getFailedConnectionAverageDuration(); // average duration of failed tasks
metrics.getTaskCount(); // total number of tasks scheduled
metrics.getRequestCount(); // total number of requests
metrics.getRequestAverageDuration(); // average request duration
metrics.getTaskAverageDuration(); // average task duration

    */

}
