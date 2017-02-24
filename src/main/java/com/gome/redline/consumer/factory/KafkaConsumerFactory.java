package com.gome.redline.consumer.factory;

import com.gome.redline.KafkaMessageType;
import com.gome.redline.consumer.AbstractKafkaConsumer;
import com.gome.redline.consumer.IKafkaConsumer;
import com.gome.redline.consumer.KafkaBeanConsumer;
import com.gome.redline.consumer.KafkaMapConsumer;
import com.gome.redline.consumer.KafkaStringConsumer;
import com.gome.redline.consumer.handler.IKafkaConsumerHandler;
import com.gome.redline.consumer.handler.KafkaConsumerBeanHandler;
import com.gome.redline.consumer.handler.KafkaConsumerMapHandler;
import com.gome.redline.consumer.handler.KafkaConsumerStringHandler;
import com.gome.redline.decoder.IKafkaMessageDecoder;
import com.gome.redline.model.KafkaMessageSendable;

/**
 * Created by Lanxiaowei at 2017/2/23 18:25
 * Kafka Consumer Factory
 */
public class KafkaConsumerFactory {
    private KafkaConsumerFactory() {}

    /**从Kafka拉取消息的最大等待超时时间，设置为-1即表示从配置文件中读取此参数值*/
    private static final long DEFAULT_POOL_MESSAGE_WAIT_TIMEOUT = -1;

    private static class SingletonHolder {
        private static final KafkaConsumerFactory INSTANCE = new KafkaConsumerFactory();
    }

    public static final KafkaConsumerFactory getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public <T extends KafkaMessageSendable> IKafkaConsumer createConsumer(KafkaMessageType messageType, String topics, IKafkaConsumerHandler handler,
                                         IKafkaMessageDecoder decoder,long poolMessageWaitTimeout,
                                         String charset,Class<T> beanClass) {
        if(null == messageType) {
            return new KafkaStringConsumer(topics,null,handler,decoder,poolMessageWaitTimeout,charset);
        }
        if(KafkaMessageType.String.equals(messageType)) {
            return new KafkaStringConsumer(topics,null,handler,decoder,poolMessageWaitTimeout,charset);
        }
        if(KafkaMessageType.Map.equals(messageType)) {
            return new KafkaMapConsumer(topics,null,handler,decoder,poolMessageWaitTimeout,charset);
        }
        if(KafkaMessageType.Bean.equals(messageType)) {
            return new KafkaBeanConsumer<T>(topics,null,handler,decoder,poolMessageWaitTimeout,charset,beanClass);
        }
        return new KafkaStringConsumer(topics,null,handler,decoder,poolMessageWaitTimeout,charset);
    }

    public <T extends KafkaMessageSendable> IKafkaConsumer createConsumer(KafkaMessageType messageType,
                                         String topics, IKafkaConsumerHandler handler,
                                         IKafkaMessageDecoder decoder,long poolMessageWaitTimeout,Class<T> beanClass) {
        return createConsumer(messageType,topics,handler,decoder,poolMessageWaitTimeout,null,beanClass);
    }

    public <T extends KafkaMessageSendable> IKafkaConsumer createConsumer(KafkaMessageType messageType,
                                         String topics, IKafkaConsumerHandler handler,
                                         IKafkaMessageDecoder decoder,Class<T> beanClass) {
        return createConsumer(messageType,topics,handler,decoder,DEFAULT_POOL_MESSAGE_WAIT_TIMEOUT,null,beanClass);
    }

    public <T extends KafkaMessageSendable> IKafkaConsumer createConsumer(KafkaMessageType messageType,
                                          String topics, IKafkaConsumerHandler handler,Class<T> beanClass) {
        return createConsumer(messageType,topics,handler,(IKafkaMessageDecoder)null,
                DEFAULT_POOL_MESSAGE_WAIT_TIMEOUT,null,beanClass);
    }

    public <T extends KafkaMessageSendable> IKafkaConsumer createConsumer(KafkaMessageType messageType, String topics,
                                          IKafkaMessageDecoder decoder,Class<T> beanClass) {
        return createConsumer(messageType,topics,(IKafkaConsumerHandler)null,decoder,
                DEFAULT_POOL_MESSAGE_WAIT_TIMEOUT,null,beanClass);
    }

    public <T extends KafkaMessageSendable> IKafkaConsumer createConsumer(KafkaMessageType messageType,
                                          String topics,Class<T> beanClass) {
        return createConsumer(messageType,topics,(IKafkaConsumerHandler)null,(IKafkaMessageDecoder)null,
                DEFAULT_POOL_MESSAGE_WAIT_TIMEOUT,null,beanClass);
    }

    public <T extends KafkaMessageSendable> IKafkaConsumer createConsumer(String topics,Class<T> beanClass) {
        return createConsumer(null,topics,null,null,DEFAULT_POOL_MESSAGE_WAIT_TIMEOUT,null,beanClass);
    }

    public <T extends KafkaMessageSendable> IKafkaConsumer createConsumer(String topics) {
        return createConsumer(null,topics,null,null,DEFAULT_POOL_MESSAGE_WAIT_TIMEOUT,null,null);
    }
}
