package com.gome.redline.producer;

import com.gome.redline.encoder.KafkaMessageStringEncoder;
import com.gome.redline.pool.IKafkaProducerPool;

/**
 * Created by Lanxiaowei at 2017/2/23 20:50
 * Kafka Producer Implement,it only can send string kafka message.
 */
public class KafkaStringProducer extends DefaultKafkaProducer<String> {
    public KafkaStringProducer(IKafkaProducerPool pool) {
        super(pool);
    }

    /**
     * 发送Kafka消息，消息类型为String字符串
     * @param msg
     * @param charset
     */
    public void send(String msg,String charset) {
        send(msg,new KafkaMessageStringEncoder(charset));
    }
}
