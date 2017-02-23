package com.gome.redline.producer;

import com.gome.redline.pool.KafkaProducerPool;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;

/**
 * Created by Lanxiaowei at 2017/2/22 20:30
 * Kafka Producer Default Implement,it only can send String kafka message.
 */
public abstract class DefaultKafkaProducer<T> implements IKafkaProducer<T> {
    protected Producer<String, byte[]> producer;
    protected KafkaProducerPool pool;

    public DefaultKafkaProducer(KafkaProducerPool pool) {
        super();
        this.producer = new KafkaProducer<String, byte[]>(pool.getConfig().getKafkaConfig());
        this.pool = pool;
    }

    /**
     * 发送Kafka消息，消息类型为字节数组
     * @param msg
     */
    public void send(byte[] msg) {
        ProducerRecord<String, byte[]> data = new ProducerRecord<String, byte[]>(this.pool.getTopic(), msg);
        this.producer.send(data);
    }

    /**
     * 将Kafka 生产者归还到Kafka Pool中，并没有真正去Close Kafka生产者
     */
    public void close() {
        this.pool.returnProducer(this);
    }

    /**
     * 关闭Kafka生产者
     */
    public void shutDown() {
        this.producer.close();
    }

    private String getTopic() {
        return this.pool.getTopic();
    }
}
