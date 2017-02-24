package com.gome.redline.pool;

import com.gome.redline.config.Configurable;
import com.gome.redline.producer.IKafkaProducer;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by Lanxiaowei at 2017/2/24 12:22
 */
public interface IKafkaProducerPool<T extends IKafkaProducer> extends Configurable {
    /**
     * Get a Producer from the pool within the given timeout
     * @param waitTimeMillis
     * how long should it wait for getting the Producer instance
     * @return a Kafka Producer instance
     */
    IKafkaProducer getProducer(long waitTimeMillis);

    IKafkaProducer getProducer();

    /**
     * return Kafka Producer to the pool.
     * @param producer
     */
    void returnProducer(IKafkaProducer producer);

    /**
     * Close the ProducerPool
     */
    void close();

    /**
     * return the pool size
     * @return
     */
    int getPoolSize();

    /**
     * return the topic that current pool monitored
     * @return
     */
    String getTopic();

    /**
     * return the Kafka Producer pool queue
     * @return
     */
    LinkedBlockingQueue<IKafkaProducer> getQueue();

    /**
     * return the Kafka Producer Class type.
     * @return
     */
    Class<T> getProducerClass();
}
