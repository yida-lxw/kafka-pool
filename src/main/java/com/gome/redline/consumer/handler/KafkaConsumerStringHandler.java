package com.gome.redline.consumer.handler;

import com.gome.redline.decoder.IKafkaMessageDecoder;
import org.apache.kafka.clients.consumer.ConsumerRecord;

/**
 * Created by Lanxiaowei at 2017/2/23 16:07
 */
public class KafkaConsumerStringHandler extends AbstractKafkaConsumerHandler<String> {
    public KafkaConsumerStringHandler() {
        super();
    }

    public KafkaConsumerStringHandler(String charset) {
        super(charset);
    }

    public void handle(ConsumerRecord<String, byte[]> record, IKafkaMessageDecoder<String> decoder) {
        String val = decoder.decode(record.value());
        System.out.printf("offset = %d, key = %s, value = %s \n",
                record.offset(), record.key(), val);
    }
}
