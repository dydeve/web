package dydeve.monitor.setting;

import org.apache.kafka.clients.producer.ProducerConfig;

/**
 * Created by dy on 2017/8/1.
 */
public class KafkaProducerSetting {

    /**
     * {@link ProducerConfig#BOOTSTRAP_SERVERS_CONFIG}
     * host1:port1,host2:port2,
     */
    private String bootstrapServers;

    /**
     * @see ProducerConfig#KEY_SERIALIZER_CLASS_DOC
     */
    private String keySerializer;

    /**
     * @see ProducerConfig#VALUE_SERIALIZER_CLASS_DOC
     */
    private String valueSerializer;

    /**
     * @see ProducerConfig#ACKS_DOC
     * default 1
     * [all, -1, 0, 1]
     */
    private String acks;

    /**
     * @see ProducerConfig#BUFFER_MEMORY_DOC
     * default 33554432 32m
     */
    private Long bufferMemory;

    private boolean switchOn;

    public String getBootstrapServers() {
        return bootstrapServers;
    }

    public void setBootstrapServers(String bootstrapServers) {
        this.bootstrapServers = bootstrapServers;
    }

    public String getKeySerializer() {
        return keySerializer;
    }

    public void setKeySerializer(String keySerializer) {
        this.keySerializer = keySerializer;
    }

    public String getValueSerializer() {
        return valueSerializer;
    }

    public void setValueSerializer(String valueSerializer) {
        this.valueSerializer = valueSerializer;
    }

    public String getAcks() {
        return acks;
    }

    public void setAcks(String acks) {
        this.acks = acks;
    }

    public Long getBufferMemory() {
        return bufferMemory;
    }

    public void setBufferMemory(Long bufferMemory) {
        this.bufferMemory = bufferMemory;
    }

    public boolean isSwitchOn() {
        return switchOn;
    }

    public void setSwitchOn(boolean switchOn) {
        this.switchOn = switchOn;
    }
}
