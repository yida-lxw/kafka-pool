package com.gome.redline.decoder;

import com.gome.redline.model.KafkaMessageSendable;
import java.lang.reflect.ParameterizedType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Lanxiaowei at 2017/2/24 16:36
 * Decode kafka message target type
 */
public class KafkaMessageBeanDecoder<T extends KafkaMessageSendable> extends AbstractKafkaMessageBeanDecoder<T> {
    private static final Logger log = LoggerFactory.getLogger(KafkaMessageBeanDecoder.class);

    public KafkaMessageBeanDecoder() {
        this(null);
    }

    public KafkaMessageBeanDecoder(String charset) {
        this(charset,null);
    }

    public KafkaMessageBeanDecoder(String charset,Class<T> beanClass) {
        super(charset,beanClass);
    }
}
