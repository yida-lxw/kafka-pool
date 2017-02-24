package com.gome.redline.encoder;

import com.gome.redline.model.KafkaMessageSendable;
import com.gome.redline.utils.json.FastJSONUtils;
import java.io.UnsupportedEncodingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Lanxiaowei at 2017/2/22 20:23
 * Encode JavaBean message to byte[]
 */
public class KafkaMessageBeanEncoder<T extends KafkaMessageSendable> extends AbstractKafkaMessageEncoder<T> {
    private static final Logger log = LoggerFactory.getLogger(KafkaMessageBeanEncoder.class);

    public KafkaMessageBeanEncoder() {
        super();
    }

    public KafkaMessageBeanEncoder(String charset) {
        super(charset);
    }

    public byte[] encode(T msg) {
        byte[] ret = null;
        try {
            String json = FastJSONUtils.beanToString(msg);
            ret = json.getBytes(this.charset);
        } catch (UnsupportedEncodingException e) {
            log.error("Encode JavaBean message to byte[] occur exception,error message:{}",e.getMessage());
        }
        return ret;
    }
}
