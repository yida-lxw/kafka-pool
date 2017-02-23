package com.gome.redline.pool;

import com.gome.redline.producer.IKafkaProducer;
import com.gome.redline.producer.KafkaStringProducer;
import java.util.concurrent.Callable;

/**
 * Created by Lanxiaowei at 2017/2/22 20:39
 */
public class PoolOfferCallable<T> implements Callable<Boolean> {
    private KafkaProducerPool pool;

    public PoolOfferCallable(KafkaProducerPool pool) {
        this.pool = pool;
    }

    public Boolean call() throws Exception {
        IKafkaProducer producer = new KafkaStringProducer(this.pool);
        if(null != producer) {
            try {
                this.pool.getQueue().offer(producer);
            } catch(RuntimeException e) {
                return false;
            }
            return true;
        }
        return false;
    }
}
