package com.gome.redline.consumer.handler;

import com.gome.redline.utils.Constant;

/**
 * Created by Lanxiaowei at 2017/2/24 17:02
 * Kafka Consumer Handler Abstract Implement
 */
public abstract class AbstractKafkaConsumerHandler<T> implements IKafkaConsumerHandler<T> {
    protected String charset;

    public AbstractKafkaConsumerHandler() {
        this(Constant.DEFAULT_CHARSET);
    }

    public AbstractKafkaConsumerHandler(String charset) {
        this.charset = charset;
    }
}
