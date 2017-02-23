package com.gome.redline.producer;

import com.gome.redline.encoder.IKafkaMessageEncoder;
import com.gome.redline.pool.KafkaProducerPool;
import java.util.Map;

/**
 * Created by Lanxiaowei at 2017/2/22 20:30
 * Kafka Producer Implement,it only can send Map<String,Object> kafka message.
 */
public class KafkaMapProducer extends DefaultKafkaProducer<Map<String,Object>> {
    public KafkaMapProducer(KafkaProducerPool pool) {
        super(pool);
    }

    /**
     * 发送Kafka消息，消息类型为Map<String, Object>
     * @param msg
     * @param encoder
     */
    public void send(Map<String, Object> msg, IKafkaMessageEncoder<Map<String, Object>> encoder) {
        byte[] encoded = encoder.encode(msg);
        send(encoded);
    }
}
