package com.gome.redline.decoder;

import java.io.UnsupportedEncodingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Lanxiaowei at 2017/2/22 20:23
 * Decode kafka message target type
 */
public class KafkaMessageStringDecoder extends AbstractKafkaMessageDecoder<String> {
    private static final Logger log = LoggerFactory.getLogger(KafkaMessageStringDecoder.class);

    public KafkaMessageStringDecoder() {
        super();
    }

    public KafkaMessageStringDecoder(String charset) {
        super(charset);
    }

    public String decode(byte[] msg) {
        String target = null;
        try {
            target = new String(msg,this.charset);
        } catch (UnsupportedEncodingException e) {
            log.error("Decode kafka message to String occur exception,error message:{}",msg);
        }
        return target;
    }
}
