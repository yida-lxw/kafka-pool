package com.gome.redline.decoder;

import com.gome.redline.utils.Constant;

/**
 * Created by Lanxiaowei at 2017/2/24 15:51
 */
public abstract class AbstractKafkaMessageDecoder<T> implements IKafkaMessageDecoder<T> {
    protected String charset;

    public AbstractKafkaMessageDecoder() {
        this(Constant.DEFAULT_CHARSET);
    }

    public AbstractKafkaMessageDecoder(String charset) {
        this.charset = charset;
    }

    protected IKafkaMessageDecoder messageDecoder;

    /************************************ Decorator Pattern ********************************/
    protected AbstractKafkaMessageDecoder(IKafkaMessageDecoder messageDecoder) {
        this.messageDecoder = messageDecoder;
    }
}
