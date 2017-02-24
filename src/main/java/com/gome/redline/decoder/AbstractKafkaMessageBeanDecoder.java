package com.gome.redline.decoder;

import com.gome.redline.model.KafkaMessageSendable;
import com.gome.redline.utils.json.FastJSONUtils;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.ParameterizedType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Lanxiaowei at 2017/2/24 16:36
 * Decode kafka message target type
 */
public abstract class AbstractKafkaMessageBeanDecoder<T extends KafkaMessageSendable> extends AbstractKafkaMessageDecoder<T> {
    private static final Logger log = LoggerFactory.getLogger(AbstractKafkaMessageBeanDecoder.class);

    protected Class<T> beanClass;

    public AbstractKafkaMessageBeanDecoder() {
        this(null,null);
    }

    public AbstractKafkaMessageBeanDecoder(String charset) {
        this(charset,null);
    }

    public AbstractKafkaMessageBeanDecoder(String charset,Class<T> beanClass) {
        super(charset);
        this.beanClass = beanClass;
    }

    public T decode(byte[] msg) {
        T t = null;
        try {
            String json = new String(msg,this.charset);
            t = FastJSONUtils.toBean(json,this.beanClass);
        } catch (UnsupportedEncodingException e) {
            log.error("Decode kafka message to 泛型T:[{}] occur exception,error message:{}",this.beanClass,msg);
        }
        return t;
    }
}
