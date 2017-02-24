package com.gome.redline.pool.callable;

import com.gome.redline.producer.IKafkaProducer;
import java.util.concurrent.Callable;

/**
 * Created by Lanxiaowei at 2017/2/22 20:39
 * Pool Offer Callable Interface,use to create Kafka Producer instance and
 * add to the Kafka Pool queue in a separate thread.
 */
public interface IPoolOfferCallable<T extends IKafkaProducer> extends Callable<Boolean> {

}
