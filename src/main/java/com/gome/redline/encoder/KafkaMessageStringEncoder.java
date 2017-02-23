package com.gome.redline.encoder;

import com.gome.redline.utils.Constant;
import java.io.UnsupportedEncodingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Lanxiaowei at 2017/2/22 20:23
 * Encode String message to byte[]
 */
public class KafkaMessageStringEncoder implements IKafkaMessageEncoder<String> {
    private static final Logger log = LoggerFactory.getLogger(KafkaMessageStringEncoder.class);
    private String charset;

    public KafkaMessageStringEncoder() {
        this.charset = Constant.DEFAULT_CHARSET;
    }

    public KafkaMessageStringEncoder(String charset) {
        this.charset = charset;
    }

    public byte[] encode(String msg) {
        byte[] ret = null;
        try {
            ret = msg.getBytes(this.charset);
        } catch (UnsupportedEncodingException e) {
            log.error("Encode String message to byte[] occur exception,message:{}",e.getMessage());
        }
        return ret;
    }

    public String getCharset() {
        return charset;
    }

    public void setCharset(String charset) {
        this.charset = charset;
    }
}
