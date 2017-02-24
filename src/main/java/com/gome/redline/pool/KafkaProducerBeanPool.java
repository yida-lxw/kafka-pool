package com.gome.redline.pool;

import com.gome.redline.pool.callable.IPoolOfferCallable;
import com.gome.redline.pool.callable.PoolOfferBeanCallable;
import com.gome.redline.pool.callable.PoolOfferMapCallable;
import com.gome.redline.producer.KafkaBeanProducer;
import com.gome.redline.producer.KafkaMapProducer;

/**
 * Created by Lanxiaowei at 2017/2/24 13:20
 */
public class KafkaProducerBeanPool extends AbstractKafkaProducerPool<KafkaBeanProducer> {

    public KafkaProducerBeanPool(int poolSize, String topic, String brokerStr) {
        super(poolSize, topic, brokerStr);
    }

    public KafkaProducerBeanPool(int poolSize, String topic) {
        super(poolSize, topic);
    }

    public KafkaProducerBeanPool(String topic) {
        super(topic);
    }

    protected IPoolOfferCallable createPoolOfferCallable() {
        return new PoolOfferBeanCallable(this);
    }
}
