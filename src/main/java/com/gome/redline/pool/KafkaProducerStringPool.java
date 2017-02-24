package com.gome.redline.pool;

import com.gome.redline.pool.callable.IPoolOfferCallable;
import com.gome.redline.pool.callable.PoolOfferStringCallable;
import com.gome.redline.producer.KafkaStringProducer;

/**
 * Created by Lanxiaowei at 2017/2/24 13:20
 */
public class KafkaProducerStringPool extends AbstractKafkaProducerPool<KafkaStringProducer> {

    public KafkaProducerStringPool(int poolSize, String topic, String brokerStr) {
        super(poolSize, topic, brokerStr);
    }

    public KafkaProducerStringPool(int poolSize, String topic) {
        super(poolSize, topic);
    }

    public KafkaProducerStringPool(String topic) {
        super(topic);
    }

    protected IPoolOfferCallable createPoolOfferCallable() {
        return new PoolOfferStringCallable(this);
    }
}
