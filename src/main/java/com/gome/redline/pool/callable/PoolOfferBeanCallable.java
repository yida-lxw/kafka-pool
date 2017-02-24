package com.gome.redline.pool.callable;

import com.gome.redline.pool.IKafkaProducerPool;
import com.gome.redline.producer.KafkaBeanProducer;
import com.gome.redline.producer.KafkaMapProducer;

/**
 * Created by Lanxiaowei at 2017/2/24 13:14
 */
public class PoolOfferBeanCallable extends AbstractPoolOfferCallable<KafkaBeanProducer> {
    public PoolOfferBeanCallable(IKafkaProducerPool pool) {
        super(pool);
    }
}
