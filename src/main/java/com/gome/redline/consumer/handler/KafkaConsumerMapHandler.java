package com.gome.redline.consumer.handler;

import com.gome.redline.decoder.IKafkaMessageDecoder;
import com.gome.redline.utils.json.FastJSONUtils;
import java.util.Map;
import org.apache.kafka.clients.consumer.ConsumerRecord;

/**
 * Created by Lanxiaowei at 2017/2/23 16:07
 */
public class KafkaConsumerMapHandler extends AbstractKafkaConsumerHandler<Map<String,Object>> {
    public KafkaConsumerMapHandler() {
        super();
    }

    public KafkaConsumerMapHandler(String charset) {
        super(charset);
    }

    public void handle(ConsumerRecord<String, byte[]> record, IKafkaMessageDecoder<Map<String, Object>> decoder) {
        //将Kafka Message解码为Map<String,Object>
        Map<String, Object> map = decoder.decode(record.value());
        if(null == map || map.isEmpty()) {
            return;
        }
        //Cast Map to JSON String
        String val = FastJSONUtils.toJSONString(map);
        System.out.printf("offset = %d, key = %s, value = %s \n",
                record.offset(), record.key(), val);
    }
}
