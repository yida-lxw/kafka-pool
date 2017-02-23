package com.gome.redline.decoder;

import com.gome.redline.utils.Constant;
import java.io.UnsupportedEncodingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Lanxiaowei at 2017/2/22 20:23
 * Decode kafka message target type
 */
public class KafkaMessageStringDecoder implements IKafkaMessageDecoder<String> {
    private static final Logger log = LoggerFactory.getLogger(KafkaMessageStringDecoder.class);
    private String charset;

    public KafkaMessageStringDecoder() {
        this.charset = Constant.DEFAULT_CHARSET;
    }

    public KafkaMessageStringDecoder(String charset) {
        this.charset = charset;
    }

    public String decode(byte[] msg) {
        String target = null;
        try {
            target = new String(msg,this.charset);
        } catch (UnsupportedEncodingException e) {
            log.error("Decode kafka message to String occur exception,message:{}",msg);
        }
        return target;
    }

    public String getCharset() {
        return charset;
    }

    public void setCharset(String charset) {
        this.charset = charset;
    }
}
