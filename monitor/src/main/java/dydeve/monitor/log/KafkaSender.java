package dydeve.monitor.log;

import dydeve.monitor.stat.IStat;
import dydeve.monitor.stat.Tracer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.KafkaException;
import org.apache.kafka.common.errors.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.concurrent.*;

/**
 * Created by dy on 2017/8/1.
 */
//todo modify here
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

    private static final ExecutorService monitorPool = new ThreadPoolExecutor(
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

    public <E, S extends IStat<E, String>> void send(Tracer<E, String, S> tracer) {
        S stat = tracer.poll();
        while (stat != null) {
            stat.setAttribute(IStat.host, tracer.getHost());
            stat.setAttribute(IStat.traceId, tracer.getTraceId());

            send(tracer.getGroup().groupName, stat.snapShot().transfer());

            stat = tracer.poll();
        }
    }

    public void send(String topic, String value) {

        if (monitorProducer == null) {
            return;
        }
        monitorPool.submit(new SendToKafkaTask(monitorProducer, topic, value));

    }


    static class SendToKafkaTask implements Runnable {

        private Producer<String, String> producer;
        private String topic;
        private String value;

        SendToKafkaTask(Producer<String, String> producer, String topic, String value) {
            this.producer = producer;
            this.topic = topic;
            this.value = value;
        }

        @Override
        public void run() {
            send(producer, topic, value);
        }
    }

    /**
     * @throws InterruptException If the thread is interrupted while blocked
     * @throws SerializationException If the key or value are not valid objects given the configured serializers
     * @throws org.apache.kafka.common.errors.TimeoutException If the time taken for fetching metadata or allocating memory for the record has surpassed <code>max.block.ms</code>.
     * @throws KafkaException If a Kafka related error occurs that does not belong to the public API exceptions.
     * @param producer
     * @param topic
     * @param data
     */
    public static void send(Producer<String, String> producer, String topic, String data) {
        producer.send(new ProducerRecord<>(topic, data));
    }

}
