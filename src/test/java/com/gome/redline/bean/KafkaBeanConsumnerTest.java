package com.gome.redline.bean;

import com.gome.redline.KafkaMessageType;
import com.gome.redline.consumer.IKafkaConsumer;
import com.gome.redline.consumer.factory.KafkaConsumerFactory;

/**
 * Created by Lanxiaowei at 2017/2/24 18:50
 * KafkaBeanConsumner Test
 */
public class KafkaBeanConsumnerTest {
    public static void main(String[] args ) {
        String topic = "bean-topic";
        //请注意: 你的JavaBean需要提供无参构造函数
        IKafkaConsumer consumer = KafkaConsumerFactory.getInstance()
                .createConsumer(KafkaMessageType.Bean,topic,Person.class);
        //订阅指定的Topic，多个Topic请使用逗号分割，比如"aaa,bbb,ccc,ddd,eee,fff"
        consumer.subscribe(topic);
        new Thread(consumer).start();
    }
}
