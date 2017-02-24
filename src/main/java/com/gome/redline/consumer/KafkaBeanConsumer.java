package com.gome.redline.consumer;

import com.gome.redline.consumer.handler.IKafkaConsumerHandler;
import com.gome.redline.consumer.handler.KafkaConsumerBeanHandler;
import com.gome.redline.consumer.handler.KafkaConsumerMapHandler;
import com.gome.redline.decoder.IKafkaMessageDecoder;
import com.gome.redline.decoder.KafkaMessageBeanDecoder;
import com.gome.redline.decoder.KafkaMessageMapDecoder;
import com.gome.redline.model.KafkaMessageSendable;
import java.lang.reflect.ParameterizedType;
import java.util.Map;

/**
 * Created by Lanxiaowei at 2017/2/24 17:52
 */
public class KafkaBeanConsumer<T extends KafkaMessageSendable> extends AbstractKafkaBeanConsumer<T> {

    public KafkaBeanConsumer(String topics) {
        super(topics);
    }

    public KafkaBeanConsumer(String topics, Class<T> beanClass) {
        super(topics, beanClass);
    }

    public KafkaBeanConsumer(String topics, String charset) {
        super(topics, charset);
    }

    public KafkaBeanConsumer(String topics, String charset, Class<T> beanClass) {
        super(topics, charset, beanClass);
    }

    public KafkaBeanConsumer(String topics, IKafkaConsumerHandler consumerHandler) {
        super(topics, consumerHandler);
    }

    public KafkaBeanConsumer(String topics, IKafkaConsumerHandler consumerHandler, Class<T> beanClass) {
        super(topics, consumerHandler, beanClass);
    }

    public KafkaBeanConsumer(String topics, IKafkaConsumerHandler consumerHandler, IKafkaMessageDecoder decoder) {
        super(topics, consumerHandler, decoder);
    }

    public KafkaBeanConsumer(String topics, IKafkaConsumerHandler consumerHandler, IKafkaMessageDecoder decoder, Class<T> beanClass) {
        super(topics, consumerHandler, decoder, beanClass);
    }

    public KafkaBeanConsumer(String topics, IKafkaConsumerHandler consumerHandler, long poolMessageWaitTimeout, String charset) {
        super(topics, consumerHandler, poolMessageWaitTimeout, charset);
    }

    public KafkaBeanConsumer(String topics, IKafkaConsumerHandler consumerHandler, long poolMessageWaitTimeout, String charset, Class<T> beanClass) {
        super(topics, consumerHandler, poolMessageWaitTimeout, charset, beanClass);
    }

    public KafkaBeanConsumer(String topics, IKafkaConsumerHandler consumerHandler, IKafkaMessageDecoder decoder, long poolMessageWaitTimeout) {
        super(topics, consumerHandler, decoder, poolMessageWaitTimeout);
    }

    public KafkaBeanConsumer(String topics, IKafkaConsumerHandler consumerHandler, IKafkaMessageDecoder decoder, long poolMessageWaitTimeout, Class<T> beanClass) {
        super(topics, consumerHandler, decoder, poolMessageWaitTimeout, beanClass);
    }

    public KafkaBeanConsumer(String topics, String groupId, IKafkaConsumerHandler consumerHandler, long poolMessageWaitTimeout, String charset) {
        super(topics, groupId, consumerHandler, poolMessageWaitTimeout, charset);
    }

    public KafkaBeanConsumer(String topics, String groupId, IKafkaConsumerHandler consumerHandler, long poolMessageWaitTimeout, String charset, Class<T> beanClass) {
        super(topics, groupId, consumerHandler, poolMessageWaitTimeout, charset, beanClass);
    }

    public KafkaBeanConsumer(String topics, String groupId, IKafkaConsumerHandler consumerHandler, IKafkaMessageDecoder decoder, long poolMessageWaitTimeout, String charset) {
        super(topics, groupId, consumerHandler, decoder, poolMessageWaitTimeout, charset);
    }

    public KafkaBeanConsumer(String topics, String groupId, IKafkaConsumerHandler consumerHandler, IKafkaMessageDecoder decoder, long poolMessageWaitTimeout, String charset, Class<T> beanClass) {
        super(topics, groupId, consumerHandler, decoder, poolMessageWaitTimeout, charset, beanClass);
    }

    protected IKafkaConsumerHandler createConsumerHandler(String charset) {
        return new KafkaConsumerBeanHandler<T>(charset);
    }

    protected IKafkaMessageDecoder createMessageDecoder(String charset) {
        return new KafkaMessageBeanDecoder<T>(charset,this.beanClass);
    }
}
