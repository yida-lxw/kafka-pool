package com.gome.redline.consumer;

import com.gome.redline.consumer.handler.IKafkaConsumerHandler;
import com.gome.redline.consumer.handler.KafkaConsumerMapHandler;
import com.gome.redline.consumer.handler.KafkaConsumerStringHandler;
import com.gome.redline.decoder.IKafkaMessageDecoder;
import com.gome.redline.decoder.KafkaMessageMapDecoder;
import com.gome.redline.decoder.KafkaMessageStringDecoder;
import java.util.Map;

/**
 * Created by Lanxiaowei at 2017/2/24 17:52
 */
public class KafkaMapConsumer extends AbstractKafkaConsumer<Map<String,Object>> {
    public KafkaMapConsumer(String topics) {
        super(topics);
    }

    public KafkaMapConsumer(String topics, String charset) {
        super(topics, charset);
    }

    public KafkaMapConsumer(String topics, IKafkaConsumerHandler consumerHandler) {
        super(topics, consumerHandler);
    }

    public KafkaMapConsumer(String topics, IKafkaConsumerHandler consumerHandler, IKafkaMessageDecoder decoder) {
        super(topics, consumerHandler, decoder);
    }

    public KafkaMapConsumer(String topics, IKafkaConsumerHandler consumerHandler, long poolMessageWaitTimeout, String charset) {
        super(topics, consumerHandler, poolMessageWaitTimeout, charset);
    }

    public KafkaMapConsumer(String topics, IKafkaConsumerHandler consumerHandler, IKafkaMessageDecoder decoder, long poolMessageWaitTimeout) {
        super(topics, consumerHandler, decoder, poolMessageWaitTimeout);
    }

    public KafkaMapConsumer(String topics, String groupId, IKafkaConsumerHandler consumerHandler, long poolMessageWaitTimeout, String charset) {
        super(topics, groupId, consumerHandler, poolMessageWaitTimeout, charset);
    }

    public KafkaMapConsumer(String topics, String groupId, IKafkaConsumerHandler consumerHandler, IKafkaMessageDecoder decoder, long poolMessageWaitTimeout, String charset) {
        super(topics, groupId, consumerHandler, decoder, poolMessageWaitTimeout, charset,null);
    }

    protected IKafkaConsumerHandler createConsumerHandler(String charset) {
        return new KafkaConsumerMapHandler(charset);
    }

    protected IKafkaMessageDecoder createMessageDecoder(String charset) {
        return new KafkaMessageMapDecoder(charset);
    }
}
