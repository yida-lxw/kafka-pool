package com.gome.redline.producer;

import com.gome.redline.encoder.IKafkaMessageEncoder;
import com.gome.redline.pool.IKafkaProducerPool;
import com.gome.redline.utils.Constant;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;

/**
 * Created by Lanxiaowei at 2017/2/22 20:30
 * Kafka Producer Default Implement,it only can send String kafka message.
 */
public abstract class DefaultKafkaProducer<T> implements IKafkaProducer<T> {
    protected Producer<String, byte[]> producer;
    protected IKafkaProducerPool pool;

    public DefaultKafkaProducer(IKafkaProducerPool pool) {
        super();
        this.producer = new KafkaProducer<String, byte[]>(pool.getConfig().getKafkaConfig());
        this.pool = pool;
    }

    /**
     * 发送Kafka消息，消息类型为字符串
     * @param msg
     * @param charset
     */
    public abstract void send(T msg,String charset);

    /**
     * 发送Kafka消息，消息类型为字节数组
     * @param msg
     */
    public void send(byte[] msg) {
        ProducerRecord<String, byte[]> data = new ProducerRecord<String, byte[]>(this.pool.getTopic(), msg);
        this.producer.send(data);
    }

    /**
     * 发送Kafka消息，消息类型为字符串
     * @param msg
     */
    public void send(T msg) {
        send(msg, Constant.DEFAULT_CHARSET);
    }

    /**
     * 发送Kafka消息，消息类型为泛型T
     * @param msg
     * @param encoder
     */
    public void send(T msg, IKafkaMessageEncoder<T> encoder) {
        byte[] encoded = encoder.encode(msg);
        send(encoded);
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
