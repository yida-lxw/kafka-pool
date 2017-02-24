package com.gome.redline.consumer;

import com.gome.redline.consumer.handler.IKafkaConsumerHandler;
import com.gome.redline.consumer.handler.KafkaConsumerStringHandler;
import com.gome.redline.decoder.IKafkaMessageDecoder;
import com.gome.redline.decoder.KafkaMessageStringDecoder;

/**
 * Created by Lanxiaowei at 2017/2/24 17:52
 */
public class KafkaStringConsumer extends AbstractKafkaConsumer<String> {
    public KafkaStringConsumer(String topics) {
        super(topics);
    }

    public KafkaStringConsumer(String topics, String charset) {
        super(topics, charset);
    }

    public KafkaStringConsumer(String topics, IKafkaConsumerHandler consumerHandler) {
        super(topics, consumerHandler);
    }

    public KafkaStringConsumer(String topics, IKafkaConsumerHandler consumerHandler, IKafkaMessageDecoder decoder) {
        super(topics, consumerHandler, decoder);
    }

    public KafkaStringConsumer(String topics, IKafkaConsumerHandler consumerHandler, long poolMessageWaitTimeout, String charset) {
        super(topics, consumerHandler, poolMessageWaitTimeout, charset);
    }

    public KafkaStringConsumer(String topics, IKafkaConsumerHandler consumerHandler, IKafkaMessageDecoder decoder, long poolMessageWaitTimeout) {
        super(topics, consumerHandler, decoder, poolMessageWaitTimeout);
    }

    public KafkaStringConsumer(String topics, String groupId, IKafkaConsumerHandler consumerHandler, long poolMessageWaitTimeout, String charset) {
        super(topics, groupId, consumerHandler, poolMessageWaitTimeout, charset);
    }

    public KafkaStringConsumer(String topics, String groupId, IKafkaConsumerHandler consumerHandler, IKafkaMessageDecoder decoder, long poolMessageWaitTimeout, String charset) {
        super(topics, groupId, consumerHandler, decoder, poolMessageWaitTimeout, charset,null);
    }

    protected IKafkaConsumerHandler createConsumerHandler(String charset) {
        return new KafkaConsumerStringHandler(charset);
    }

    protected IKafkaMessageDecoder createMessageDecoder(String charset) {
        return new KafkaMessageStringDecoder(charset);
    }
}
