package com.gome.redline.producer;

import com.gome.redline.encoder.IKafkaMessageEncoder;
import com.gome.redline.pool.KafkaProducerPool;

/**
 * Created by Lanxiaowei at 2017/2/23 20:50
 * Kafka Producer Implement,it only can send string kafka message.
 */
public class KafkaStringProducer extends DefaultKafkaProducer<String> {
    public KafkaStringProducer(KafkaProducerPool pool) {
        super(pool);
    }

    /**
     * 发送Kafka消息，消息类型为字符串
     * @param msg
     * @param encoder
     */
    public void send(String msg, IKafkaMessageEncoder<String> encoder) {
        byte[] encoded = encoder.encode(msg);
        send(encoded);
    }
}
