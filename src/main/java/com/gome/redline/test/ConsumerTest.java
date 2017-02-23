package com.gome.redline.test;

import com.gome.redline.consumer.IKafkaConsumer;
import com.gome.redline.consumer.factory.KafkaConsumerFactory;

/**
 * Created by Lanxiaowei at 2017/2/23 15:16
 * Kafka Consumer Test
 */
public class ConsumerTest {
    public static void main(String[] args ) {
        String topic = "test-topic";
        IKafkaConsumer consumer = KafkaConsumerFactory.getInstance().createConsumer(topic);
        //订阅指定的Topic，多个Topic请使用逗号分割，比如"aaa,bbb,ccc,ddd,eee,fff"
        consumer.subscribe(topic);
        new Thread(consumer).start();
    }
}
