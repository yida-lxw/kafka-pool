package com.gome.redline.pool.callable;

import com.gome.redline.pool.IKafkaProducerPool;
import com.gome.redline.producer.KafkaMapProducer;

/**
 * Created by Lanxiaowei at 2017/2/24 13:14
 */
public class PoolOfferMapCallable extends AbstractPoolOfferCallable<KafkaMapProducer> {
    public PoolOfferMapCallable(IKafkaProducerPool pool) {
        super(pool);
    }
}
