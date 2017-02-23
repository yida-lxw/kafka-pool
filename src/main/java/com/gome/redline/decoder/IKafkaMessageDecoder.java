package com.gome.redline.decoder;

/**
 * Created by Lanxiaowei at 2017/2/23 19:14
 * Kafka Message Decoder Interface
 */
public interface IKafkaMessageDecoder<T> {
    /**
     * Decode Kafka message to Target type
     * @param msg
     * @return
     */
    T decode(byte[] msg);
}
