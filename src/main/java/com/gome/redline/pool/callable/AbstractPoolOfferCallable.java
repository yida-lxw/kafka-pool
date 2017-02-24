package com.gome.redline.pool.callable;

import com.gome.redline.pool.IKafkaProducerPool;
import com.gome.redline.producer.IKafkaProducer;
import com.gome.redline.utils.common.ReflectUtils;
import java.lang.reflect.ParameterizedType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Lanxiaowei at 2017/2/24 11:45
 * Pool Offer Callable Abstract Implement
 */
public abstract class AbstractPoolOfferCallable<T extends IKafkaProducer> implements IPoolOfferCallable<T> {
    private static final Logger log = LoggerFactory.getLogger(AbstractPoolOfferCallable.class);
    private IKafkaProducerPool<T> pool;
    private Class<T> producerClass;

    public AbstractPoolOfferCallable(IKafkaProducerPool<T> pool) {
        this.pool = pool;
        this.producerClass = (Class<T>) ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        if(!this.pool.getProducerClass().equals(this.producerClass)) {
            log.error("the pool[{}]'s producer class [{}] don't equals to {}'s producerClass [{}]",
                    this.pool.getClass(),this.pool.getProducerClass(),this.getClass(),this.producerClass);
            throw new RuntimeException("Create PoolOfferCallable instance occur exception:");
        }
    }

    public Boolean call() throws Exception {
        //IKafkaProducer producer = new KafkaStringProducer(this.pool);
        //T producer = ReflectUtils.createByConstruction(this.producerClass,new Class[] {this.pool.getClass()},new Object[] {this.pool});
        T producer = ReflectUtils.createByConstruction(
                this.producerClass,
                new Class[] {IKafkaProducerPool.class},
                new Object[] {this.pool}
        );
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
