package com.gome.redline.producer;

import com.gome.redline.encoder.KafkaMessageBeanEncoder;
import com.gome.redline.model.KafkaMessageSendable;
import com.gome.redline.pool.IKafkaProducerPool;

/**
 * Created by Lanxiaowei at 2017/2/24 16:26
 */
public class KafkaBeanProducer<T extends KafkaMessageSendable> extends DefaultKafkaProducer<T> {
    public KafkaBeanProducer(IKafkaProducerPool pool) {
        super(pool);
    }

    /**
     * 发送Kafka消息，消息类型为泛型T
     * @param msg
     * @param charset
     */
    public void send(T msg, String charset) {
        send(msg,new KafkaMessageBeanEncoder(charset));
    }
}
