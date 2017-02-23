package com.gome.redline.encoder;

import com.gome.redline.utils.Constant;
import com.gome.redline.utils.json.FastJSONUtils;
import java.io.UnsupportedEncodingException;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Lanxiaowei at 2017/2/23 20:58
 * Encode Map message to byte[]
 */
public class KafkaMessageMapEncoder implements IKafkaMessageEncoder<Map<String,Object>> {
    private static final Logger log = LoggerFactory.getLogger(KafkaMessageMapEncoder.class);
    private String charset;

    public KafkaMessageMapEncoder() {
        this.charset = Constant.DEFAULT_CHARSET;
    }

    public KafkaMessageMapEncoder(String charset) {
        this.charset = charset;
    }

    public byte[] encode(Map<String,Object> msg) {
        byte[] ret = null;
        try {
            //先将Map转换成JSON字符串，再将JSON字符串转换成byte[]
            String json = FastJSONUtils.toJSONString(msg);
            ret = json.getBytes(this.charset);
        } catch (UnsupportedEncodingException e) {
            log.error("Encode Map<String,Object> message to byte[] occur exception,message:{}",e.getMessage());
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
