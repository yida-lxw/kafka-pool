package com.gome.redline.pool;

import com.gome.redline.pool.callable.IPoolOfferCallable;
import com.gome.redline.pool.callable.PoolOfferMapCallable;
import com.gome.redline.pool.callable.PoolOfferStringCallable;
import com.gome.redline.producer.KafkaMapProducer;

/**
 * Created by Lanxiaowei at 2017/2/24 13:20
 */
public class KafkaProducerMapPool extends AbstractKafkaProducerPool<KafkaMapProducer> {

    public KafkaProducerMapPool(int poolSize, String topic, String brokerStr) {
        super(poolSize, topic, brokerStr);
    }

    public KafkaProducerMapPool(int poolSize, String topic) {
        super(poolSize, topic);
    }

    public KafkaProducerMapPool(String topic) {
        super(topic);
    }

    protected IPoolOfferCallable createPoolOfferCallable() {
        return new PoolOfferMapCallable(this);
    }
}
