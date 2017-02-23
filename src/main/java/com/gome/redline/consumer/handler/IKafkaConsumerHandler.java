package com.gome.redline.consumer.handler;

import com.gome.redline.decoder.IKafkaMessageDecoder;
import org.apache.kafka.clients.consumer.ConsumerRecord;

/**
 * Created by Lanxiaowei at 2017/2/23 16:05
 * Kafka Consumer Handler
 */
public interface IKafkaConsumerHandler<T> {
    void handle(ConsumerRecord<String, byte[]> record, IKafkaMessageDecoder<T> decoder);
}
