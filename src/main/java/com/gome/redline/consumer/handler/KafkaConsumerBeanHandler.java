package com.gome.redline.consumer.handler;

import com.gome.redline.decoder.IKafkaMessageDecoder;
import com.gome.redline.model.KafkaMessageSendable;
import com.gome.redline.utils.json.FastJSONUtils;
import java.util.Map;
import org.apache.kafka.clients.consumer.ConsumerRecord;

/**
 * Created by Lanxiaowei at 2017/2/23 16:07
 */
public class KafkaConsumerBeanHandler<T extends KafkaMessageSendable> extends AbstractKafkaConsumerHandler<T> {
    public KafkaConsumerBeanHandler() {
        super();
    }

    public KafkaConsumerBeanHandler(String charset) {
        super(charset);
    }

    public void handle(ConsumerRecord<String, byte[]> record, IKafkaMessageDecoder<T> decoder) {
        //将Kafka Message解码为泛型T
        T t = decoder.decode(record.value());
        if(null == t) {
            return;
        }
        //Cast T to JSON String
        String val = FastJSONUtils.toJSONString(t);
        System.out.printf("offset = %d, key = %s, value = %s \n",
                record.offset(), record.key(), val);
    }
}
