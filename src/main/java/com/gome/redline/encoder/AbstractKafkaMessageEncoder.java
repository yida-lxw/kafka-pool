package com.gome.redline.encoder;

import com.gome.redline.utils.Constant;

/**
 * Created by Lanxiaowei at 2017/2/24 15:21
 */
public abstract class AbstractKafkaMessageEncoder<T> implements IKafkaMessageEncoder<T> {
    protected String charset;

    public AbstractKafkaMessageEncoder() {
        this.charset = Constant.DEFAULT_CHARSET;
    }

    public AbstractKafkaMessageEncoder(String charset) {
        this.charset = charset;
    }

    protected IKafkaMessageEncoder messageEncoder;

    /************************************ Decorator Pattern ********************************/
    protected AbstractKafkaMessageEncoder(IKafkaMessageEncoder messageEncoder) {
        this.messageEncoder = messageEncoder;
    }
}
