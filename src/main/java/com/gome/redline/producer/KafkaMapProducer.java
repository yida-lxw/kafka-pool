package com.gome.redline.producer;

import com.gome.redline.encoder.KafkaMessageMapEncoder;
import com.gome.redline.pool.IKafkaProducerPool;
import java.util.Map;

/**
 * Created by Lanxiaowei at 2017/2/22 20:30
 * Kafka Producer Implement,it only can send Map<String,Object> kafka message.
 */
public class KafkaMapProducer extends DefaultKafkaProducer<Map<String,Object>> {
    public KafkaMapProducer(IKafkaProducerPool pool) {
        super(pool);
    }

    /**
     * 发送Kafka消息，消息类型为Map<String, Object>
     * @param msg
     * @param charset
     */
    public void send(Map<String,Object> msg,String charset) {
        send(msg,new KafkaMessageMapEncoder(charset));
    }
}
