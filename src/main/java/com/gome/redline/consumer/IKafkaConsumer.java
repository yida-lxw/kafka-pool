package com.gome.redline.consumer;

import com.gome.redline.consumer.handler.IKafkaConsumerHandler;
import java.util.List;
import org.apache.kafka.clients.consumer.ConsumerRecords;

/**
 * Created by Lanxiaowei at 2017/2/23 15:45
 * Kafka Consumer Interface
 */
public interface IKafkaConsumer extends Runnable{
    /**
     * subscribe topics
     * @param topics
     */
    void subscribe(String topics);

    /**
     * subscribe topics
     * @param topicList
     */
    void subscribe(List<String> topicList);

    /**
     * Consume kafka message
     * @param consumerHandler
     * @param records
     */
    void consumer(IKafkaConsumerHandler consumerHandler, ConsumerRecords<String, byte[]> records);

    /**
     * Close Kafka Consumer
     */
    void close();
}
