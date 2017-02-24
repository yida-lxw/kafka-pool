package com.gome.redline.encoder;

import java.io.UnsupportedEncodingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Lanxiaowei at 2017/2/22 20:23
 * Encode String message to byte[]
 */
public class KafkaMessageStringEncoder extends AbstractKafkaMessageEncoder<String> {
    private static final Logger log = LoggerFactory.getLogger(KafkaMessageStringEncoder.class);

    public KafkaMessageStringEncoder() {
        super();
    }

    public KafkaMessageStringEncoder(String charset) {
        super(charset);
    }

    public byte[] encode(String msg) {
        byte[] ret = null;
        try {
            ret = msg.getBytes(this.charset);
        } catch (UnsupportedEncodingException e) {
            log.error("Encode String message to byte[] occur exception,error message:{}",e.getMessage());
        }
        return ret;
    }
}
