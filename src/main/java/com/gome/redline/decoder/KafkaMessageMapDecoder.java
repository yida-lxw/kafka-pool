package com.gome.redline.decoder;

import com.gome.redline.utils.Constant;
import com.gome.redline.utils.json.FastJSONUtils;
import java.io.UnsupportedEncodingException;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Lanxiaowei at 2017/2/22 20:23
 * Decode kafka message target type
 */
public class KafkaMessageMapDecoder implements IKafkaMessageDecoder<Map<String,Object>> {
    private static final Logger log = LoggerFactory.getLogger(KafkaMessageMapDecoder.class);
    private String charset;

    public KafkaMessageMapDecoder() {
        this.charset = Constant.DEFAULT_CHARSET;
    }

    public KafkaMessageMapDecoder(String charset) {
        this.charset = charset;
    }

    public Map<String,Object> decode(byte[] msg) {
        Map<String,Object> target = null;
        try {
            String json = new String(msg,this.charset);
            target = FastJSONUtils.stringToMap(json);
        } catch (UnsupportedEncodingException e) {
            log.error("Decode kafka message to Map<String,Object> occur exception,message:{}",msg);
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
