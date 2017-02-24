package com.gome.redline.bean;

import com.gome.redline.KafkaMessageType;
import com.gome.redline.pool.IKafkaProducerPool;
import com.gome.redline.pool.factory.KafkaProducerPoolFactory;
import com.gome.redline.producer.IKafkaProducer;
import com.gome.redline.utils.Constant;

/**
 * Created by Lanxiaowei at 2017/2/24 18:42
 * KafkaBeanProducer Test
 */
public class KafkaBeanProducerTest {
    public static void main(String[] args ) {
        String topic = "bean-topic";
        //通过Kafka生产池子工厂类创建Kafka生产池对象
        IKafkaProducerPool producerPool = KafkaProducerPoolFactory.getInstance()
                .createKafkaProducerPool(KafkaMessageType.Bean,topic);
        IKafkaProducer<Person> producer = producerPool.getProducer();
        if(null == producer) {
            System.out.println("take a kafka producer from Kafka Pool,but it is null.");
            return;
        }
        //创建JavaBean Kafka Message
        Person person = new Person(2,"Zhangsan",26);
        producer.send(person, Constant.DEFAULT_CHARSET);
        System.out.println("Producer send kafka message have finished.");
        //将Kafka生产者归还到Kafka Pool中
        producerPool.returnProducer(producer);
        producerPool.close();
    }
}
