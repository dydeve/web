package dydeve.monitor.config;

import dydeve.monitor.setting.KafkaProducerSetting;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by yuduy on 2017/8/1.
 */
@Configuration
public class KafkaConfig {

    private static final Logger log = LoggerFactory.getLogger(KafkaConfig.class);

    @Bean(name = "monitorProducerSetting")
    protected KafkaProducerSetting monitorProducerSetting() {
        //read from properties\xml\json\yml
        KafkaProducerSetting setting = new KafkaProducerSetting();
        setting.setAcks("0");
        setting.setBootstrapServers("localhost:9080,localhost:9090");
        setting.setBufferMemory(33554432L);
        setting.setKeySerializer(StringSerializer.class.getName());
        setting.setValueSerializer(StringSerializer.class.getName());
        setting.setSwitchOn(true);

        return setting;
    }

    /**
     * 只有switchOn配置为true才会执行
     * moblog kafka setup
     */
    @Bean(name = "monitorProducer", destroyMethod = "close")
    public Producer<String, String> monitorProducer(@Qualifier("monitorProducerSetting") KafkaProducerSetting setting) {
        log.info("monitor kafka producer start config.");
        if(!setting.isSwitchOn()){
            log.info("monitor kafka producer switch to off.");
            return null;
        }
        Map<String, Object> config = new HashMap<String, Object>();
        config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, setting.getBootstrapServers());
        config.put(ProducerConfig.ACKS_CONFIG, setting.getAcks());
        config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, setting.getValueSerializer());
        config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, setting.getKeySerializer());
        config.put(ProducerConfig.BUFFER_MEMORY_CONFIG, setting.getBufferMemory());

        return new KafkaProducer<>(config);
    }


}
