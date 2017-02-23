package com.gome.redline.consumer.factory;

import com.gome.redline.consumer.DefaultKafkaConsumerImpl;
import com.gome.redline.consumer.IKafkaConsumer;
import com.gome.redline.consumer.handler.IKafkaConsumerHandler;
import com.gome.redline.consumer.handler.KafkaConsumerStringHandler;
import com.gome.redline.decoder.IKafkaMessageDecoder;
import com.gome.redline.decoder.KafkaMessageStringDecoder;

/**
 * Created by Lanxiaowei at 2017/2/23 18:25
 * Kafka Consumer Factory
 */
public class KafkaConsumerFactory {
    private KafkaConsumerFactory() {}

    private static class SingletonHolder {
        private static final KafkaConsumerFactory INSTANCE = new KafkaConsumerFactory();
    }

    public static final KafkaConsumerFactory getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public IKafkaConsumer createConsumer(String topics, IKafkaMessageDecoder decoder,long poolMessageWaitTimeout) {
        IKafkaConsumerHandler handler = new KafkaConsumerStringHandler();
        return new DefaultKafkaConsumerImpl(topics,handler,decoder,poolMessageWaitTimeout);
    }

    public IKafkaConsumer createConsumer(String topics, IKafkaMessageDecoder decoder) {
        IKafkaConsumerHandler handler = new KafkaConsumerStringHandler();
        return new DefaultKafkaConsumerImpl(topics,handler,decoder);
    }

    public IKafkaConsumer createConsumer(String topics,long poolMessageWaitTimeout) {
        IKafkaConsumerHandler handler = new KafkaConsumerStringHandler();
        IKafkaMessageDecoder decoder = new KafkaMessageStringDecoder();
        return new DefaultKafkaConsumerImpl(topics,handler,decoder,poolMessageWaitTimeout);
    }

    public IKafkaConsumer createConsumer(String topics) {
        IKafkaConsumerHandler handler = new KafkaConsumerStringHandler();
        return new DefaultKafkaConsumerImpl(topics,handler);
    }
}
