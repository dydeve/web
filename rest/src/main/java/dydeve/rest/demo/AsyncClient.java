package dydeve.rest.demo;

import dydeve.common.util.ThrowableUtils;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.CloseableHttpPipeliningClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.apache.http.nio.IOControl;
import org.apache.http.nio.client.methods.AsyncCharConsumer;
import org.apache.http.nio.client.methods.HttpAsyncMethods;
import org.apache.http.nio.protocol.BasicAsyncRequestProducer;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.nio.CharBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * Created by yuduy on 2017/8/11.
 */
public class AsyncClient {

    public static void main(String[] args) throws InterruptedException, ExecutionException, IOException {
        demo5();
    }

    /**
     * This example demonstrates a basic asynchronous HTTP request / response exchange. Response content is buffered in memory for simplicity.
     */
    public static void demo1() throws ExecutionException, InterruptedException, IOException {
        CloseableHttpAsyncClient httpclient = HttpAsyncClients.createDefault();
        try {
            httpclient.start();
            HttpGet request = new HttpGet("http://httpbin.org/get");
            Future<HttpResponse> future = httpclient.execute(request, null);
            HttpResponse response = future.get();
            System.out.println("Response: " + response.getStatusLine());
            System.out.println(EntityUtils.toString(response.getEntity()));
            System.out.println("Shutting down");
        } finally {
            httpclient.close();
        }
        System.out.println("Done");
    }

    /**
     * This example demonstrates an asynchronous HTTP request / response exchange with a full content streaming.
     */
    public static void demo2() throws IOException, ExecutionException, InterruptedException {
        System.out.println(Thread.currentThread().getName() + " started");
        CloseableHttpAsyncClient httpclient = HttpAsyncClients.createDefault();
        try {
            httpclient.start();
            Future<Boolean> future = httpclient.execute(
                    HttpAsyncMethods.createGet("http://httpbin.org/"),
                    new MyResponseConsumer(), null);
            Boolean result = future.get();
            if (result != null && result.booleanValue()) {
                System.out.println("Request successfully executed");
            } else {
                System.out.println("Request failed");
            }
            System.out.println("Shutting down");
        } finally {
            httpclient.close();
        }
        System.out.println("Done");
    }

    /**
     * This example demonstrates a fully asynchronous execution of multiple HTTP exchanges
     * where the result of an individual operation is reported using a callback interface.
     * @throws InterruptedException
     * @throws IOException
     */
    public static void demo3() throws InterruptedException, IOException {
        RequestConfig requestConfig = RequestConfig.custom()
                .setSocketTimeout(3000)
                .setConnectTimeout(3000).build();
        CloseableHttpAsyncClient httpclient = HttpAsyncClients.custom()
                .setDefaultRequestConfig(requestConfig)
                .build();
        try {
            httpclient.start();
            final HttpGet[] requests = new HttpGet[] {
                    new HttpGet("http://httpbin.org/ip"),
                    new HttpGet("https://httpbin.org/ip"),
                    new HttpGet("http://httpbin.org/headers")
            };
            final CountDownLatch latch = new CountDownLatch(requests.length);
            for (final HttpGet request: requests) {
                httpclient.execute(request, new FutureCallback<HttpResponse>() {

                    @Override
                    public void completed(final HttpResponse response) {
                        latch.countDown();
                        System.out.println(Thread.currentThread().getName() + "  " +request.getRequestLine() + "->" + response.getStatusLine());
                    }

                    @Override
                    public void failed(final Exception ex) {
                        latch.countDown();
                        System.out.println(Thread.currentThread().getName() + "  " +request.getRequestLine() + "->" + ThrowableUtils.stackTrace(ex));
                    }

                    @Override
                    public void cancelled() {
                        latch.countDown();
                        System.out.println(Thread.currentThread().getName() + "  " +request.getRequestLine() + " cancelled");
                    }

                });
            }
            latch.await();
            System.out.println("Shutting down");
        } finally {
            httpclient.close();
        }
        System.out.println("Done");
    }

    /**
     * This example demonstrates a pipelined execution of multiple HTTP request / response exchanges. Response content is buffered in memory for simplicity.
     */
    public static void demo4() throws ExecutionException, InterruptedException, IOException {
        CloseableHttpPipeliningClient httpclient = HttpAsyncClients.createPipelining();
        try {
            httpclient.start();

            HttpHost targetHost = new HttpHost("httpbin.org", 80);
            HttpGet[] resquests = {
                    new HttpGet("/"),
                    new HttpGet("/ip"),
                    new HttpGet("/headers"),
                    new HttpGet("/get")
            };

            Future<List<HttpResponse>> future = httpclient.execute(targetHost,
                    Arrays.<HttpRequest>asList(resquests), null);
            List<HttpResponse> responses = future.get();
            for (HttpResponse response: responses) {
                System.out.println(response.getStatusLine());
            }
            System.out.println("Shutting down");
        } finally {
            httpclient.close();
        }
        System.out.println("Done");
    }

    public static void demo5() throws ExecutionException, InterruptedException, IOException {
        CloseableHttpPipeliningClient httpclient = HttpAsyncClients.createPipelining();
        try {
            httpclient.start();

            HttpHost targetHost = new HttpHost("httpbin.org", 80);
            HttpGet[] resquests = {
                    new HttpGet("/"),
                    new HttpGet("/ip"),
                    new HttpGet("/headers"),
                    new HttpGet("/get")
            };

            List<MyRequestProducer> requestProducers = new ArrayList<MyRequestProducer>();
            List<MyResponseConsumer2> responseConsumers = new ArrayList<MyResponseConsumer2>();
            for (HttpGet request: resquests) {
                requestProducers.add(new MyRequestProducer(targetHost, request));
                responseConsumers.add(new MyResponseConsumer2(request));
            }

            Future<List<Boolean>> future = httpclient.execute(
                    targetHost, requestProducers, responseConsumers, null);
            future.get();
            System.out.println("Shutting down");
        } finally {
            httpclient.close();
        }
        System.out.println("Done");
    }

    static class MyResponseConsumer extends AsyncCharConsumer<Boolean> {

        @Override
        protected void onResponseReceived(final HttpResponse response) {
            System.out.println(Thread.currentThread().getName() + "onResponseReceived");
        }

        @Override
        protected void onCharReceived(final CharBuffer buf, final IOControl ioctrl) throws IOException {
            System.out.println(Thread.currentThread().getName() + "onCharReceived");
            /*while (buf.hasRemaining()) {
                System.out.print(buf.get());
            }*/
        }

        @Override
        protected void releaseResources() {
            System.out.println(Thread.currentThread().getName() + "releaseResources");
        }

        @Override
        protected Boolean buildResult(final HttpContext context) {
            System.out.println(Thread.currentThread().getName() + "buildResult");
            return Boolean.TRUE;
        }

    }


    static class MyRequestProducer extends BasicAsyncRequestProducer {

        private final HttpRequest request;

        MyRequestProducer(final HttpHost target, final HttpRequest request) {
            super(target, request);
            this.request = request;
        }

        @Override
        public void requestCompleted(final HttpContext context) {
            super.requestCompleted(context);
            System.out.println();
            System.out.println("Request sent: " + this.request.getRequestLine() + Thread.currentThread().getName());
            System.out.println("=================================================");
        }
    }

    static class MyResponseConsumer2 extends AsyncCharConsumer<Boolean> {

        private final HttpRequest request;

        MyResponseConsumer2(final HttpRequest request) {
            this.request = request;
        }

        @Override
        protected void onResponseReceived(final HttpResponse response) {
            System.out.println();
            System.out.println("Response received: " + response.getStatusLine() + " -> " + this.request.getRequestLine() + Thread.currentThread().getName());
            System.out.println("=================================================");
        }

        @Override
        protected void onCharReceived(final CharBuffer buf, final IOControl ioctrl) throws IOException {
            while (buf.hasRemaining()) {
                System.out.print(buf.get());
            }
        }

        @Override
        protected void releaseResources() {
            System.out.println("==================release============" + Thread.currentThread().getName());
        }

        @Override
        protected Boolean buildResult(final HttpContext context) {
            System.out.println();
            System.out.println("done   =================================================" + Thread.currentThread().getName());
            System.out.println();
            return Boolean.TRUE;
        }

    }
}
