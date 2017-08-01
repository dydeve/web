package dydeve.monitor.log;

import com.alibaba.fastjson.JSON;
import dydeve.monitor.util.StopWatchMonitor;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.concurrent.*;

/**
 * Created by yuduy on 2017/8/1.
 */
@Component
public class KafkaSender {

    private static final Logger log = LoggerFactory.getLogger(KafkaSender.class);

    private static final int NCPU = Runtime.getRuntime().availableProcessors();

    private static final int MAX_QUEUE_SIZE = 102400;

    private static class MonitorThreadFactory implements ThreadFactory {

        private static final String threadNamePrefix = "kafka-monitor-sender";

        private static int threadNum = 0;
        private static synchronized int nextThreadNum() {
            return threadNum++;
        }

        @Override
        public Thread newThread(Runnable r) {
            return new Thread(threadNamePrefix + nextThreadNum());
        }
    }

    //private static class



    ExecutorService monitorPool = new ThreadPoolExecutor(
            Math.min(4, NCPU),
            Math.min(4, NCPU),
            0L,
            TimeUnit.MILLISECONDS,
            new ArrayBlockingQueue<>(MAX_QUEUE_SIZE),
            new MonitorThreadFactory(),
            new ThreadPoolExecutor.DiscardPolicy()
    );

    @Autowired
    @Qualifier("monitorProducer")
    private Producer<String, String> monitorProducer;

    @PostConstruct
    private void init() {
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                monitorPool.shutdown();
                try {
                    if (!monitorPool.awaitTermination(3, TimeUnit.SECONDS)) {
                        log.warn("monitorPool hasn't terminated in 3 seconds.");
                    }
                } catch (InterruptedException e) {
                    log.error("monitorPool's termination was interrupted.", e);
                }
            }
        });
    }

    public void send(StopWatchMonitor monitor) {
        if (monitorProducer == null) {
            return;
        }
        monitorPool.submit(new SendToKafkaTask(monitorProducer, "monitor", monitor));
    }


    static class SendToKafkaTask implements Runnable {

        private Producer<String, String> producer;
        private String topic;
        private Object data;

        SendToKafkaTask(Producer<String, String> producer, String topic, Object data) {
            this.producer = producer;
            this.topic = topic;
            this.data = data;
        }

        @Override
        public void run() {
            send(producer, topic, JSON.toJSONString(data));
        }
    }


    public static void send(Producer<String, String> producer, String topic, String data) {
        producer.send(new ProducerRecord<String, String>(topic, data));
    }

}
