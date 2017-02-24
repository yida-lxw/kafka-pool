package com.gome.redline.pool.factory;

import com.gome.redline.KafkaMessageType;
import com.gome.redline.model.KafkaMessageSendable;
import com.gome.redline.pool.IKafkaProducerPool;
import com.gome.redline.pool.KafkaProducerBeanPool;
import com.gome.redline.pool.KafkaProducerMapPool;
import com.gome.redline.pool.KafkaProducerStringPool;

/**
 * Created by Lanxiaowei at 2017/2/24 14:05
 * Kafka Producer Pool Factory
 */
public class KafkaProducerPoolFactory<T extends KafkaMessageSendable> {

    /**************************** The Singleton Pattern begin****************************/
    public KafkaProducerPoolFactory() {}

    private static class SingletonHolder {
        private static final KafkaProducerPoolFactory INSTANCE = new KafkaProducerPoolFactory();
    }

    public static final KafkaProducerPoolFactory getInstance() {
        return SingletonHolder.INSTANCE;
    }
    /**************************** The Singleton Pattern end  ****************************/


    private static final int DEFAULT_POOL_SIZE = 0;

    public IKafkaProducerPool createKafkaProducerPool(KafkaMessageType messageType,int poolSize, String topic) {
        if(null == messageType) {
            return new KafkaProducerStringPool(poolSize,topic);
        }
        if(KafkaMessageType.String.equals(messageType)) {
            return new KafkaProducerStringPool(poolSize,topic);
        }
        if(KafkaMessageType.Map.equals(messageType)) {
            return new KafkaProducerMapPool(poolSize,topic);
        }
        if(KafkaMessageType.Bean.equals(messageType)) {
            return new KafkaProducerBeanPool(poolSize,topic);
        }
        return new KafkaProducerStringPool(poolSize,topic);
    }

    public IKafkaProducerPool createKafkaProducerPool(KafkaMessageType messageType,String topic) {
        return createKafkaProducerPool(messageType,DEFAULT_POOL_SIZE,topic);
    }

    public IKafkaProducerPool createKafkaProducerPool(int poolSize,String topic) {
        return createKafkaProducerPool(null,poolSize,topic);
    }

    public IKafkaProducerPool createKafkaProducerPool(String topic) {
        return createKafkaProducerPool(null,DEFAULT_POOL_SIZE,topic);
    }
}
