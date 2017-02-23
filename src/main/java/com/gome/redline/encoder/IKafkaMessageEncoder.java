package com.gome.redline.encoder;

/**
 * Created by Lanxiaowei at 2017/2/22 20:21
 * Kafka Message Encoder Interface
 */
public interface IKafkaMessageEncoder<T> {
    /**
     * Encode Kafka message to byte array.
     * @param msg
     * @return
     */
    byte[] encode(T msg);
}
