package com.gome.redline.pool.callable;

import com.gome.redline.pool.IKafkaProducerPool;
import com.gome.redline.producer.KafkaStringProducer;

/**
 * Created by Lanxiaowei at 2017/2/24 13:14
 */
public class PoolOfferStringCallable extends AbstractPoolOfferCallable<KafkaStringProducer> {
    public PoolOfferStringCallable(IKafkaProducerPool pool) {
        super(pool);
    }
}
