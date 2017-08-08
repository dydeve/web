package dydeve.rest.config;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.config.SocketConfig;
import org.apache.http.conn.DnsResolver;
import org.apache.http.conn.HttpConnectionFactory;
import org.apache.http.conn.ManagedHttpClientConnection;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.DefaultConnectionReuseStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultConnectionKeepAliveStrategy;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.ManagedHttpClientConnectionFactory;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.impl.conn.SystemDefaultDnsResolver;
import org.apache.http.impl.io.DefaultHttpRequestWriterFactory;
import org.apache.http.impl.io.DefaultHttpResponseParserFactory;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * Created by yuduy on 2017/8/8.
 */
public class Client {

    private static final Logger log = LoggerFactory.getLogger(Client.class);


    static PoolingHttpClientConnectionManager manager = null;
    static CloseableHttpClient httpClient;

    public static CloseableHttpClient getHttpClient() {
        if (httpClient == null) {

            synchronized (Client.class) {
                if (httpClient == null) {
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
                    manager = new PoolingHttpClientConnectionManager(socketFactoryRegistry, connectionFactory, dnsResolver);
                    //默认为socket配置
                    SocketConfig defaultSocketConfig = SocketConfig.custom().setTcpNoDelay(true).build();
                    manager.setDefaultSocketConfig(defaultSocketConfig);

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

                    //默认请求配置
                    RequestConfig defaultRequestConfig = RequestConfig.custom()
                            .setConnectTimeout(2 * 1000)//连接超时时间 2s
                            .setSocketTimeout(5 * 1000)//等待数据超时时间 5s
                            .setConnectionRequestTimeout(2000)//从连接池获取连接的等待时间
                            .build();

                    //创建httpClient
                    httpClient = HttpClients.custom()
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

                    //JVM停止或重启时，关闭连接池释放掉连接(跟数据库连接池类似)
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
                    });
                }
            }

        }

        return httpClient;

    }

    public static void consume() {
        HttpResponse response = null;
        try {
            HttpGet get = new HttpGet("http://item.jd.com/2381431.html");
            response = getHttpClient().execute(get);
            if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
                EntityUtils.consume(response.getEntity());
                //error
            } else {
                String result = EntityUtils.toString(response.getEntity());
                System.out.println(result);
            }
        } catch (Exception e) {
            log.error("haha", e);
            if (response != null) {
                try {
                    EntityUtils.consume(response.getEntity());
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }

    public static void main(String[] args) {
        consume();
    }


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
}
