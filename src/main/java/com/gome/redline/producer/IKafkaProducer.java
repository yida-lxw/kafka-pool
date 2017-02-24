package com.gome.redline.producer;

import com.gome.redline.encoder.IKafkaMessageEncoder;

/**
 * Created by Lanxiaowei at 2017/2/22 20:19
 * Kafka Producer Interface
 */
public interface IKafkaProducer<T> {
    /**
     * Send message to Kafka
     * @param msg
     */
    void send(byte[] msg);

    /**
     * Send message to Kafka with MessageEncoder
     * @param msg
     * @param encoder
     */
    void send(T msg, IKafkaMessageEncoder<T> encoder);

    void send(T msg,String charset);

    void send(T msg);

    /**
     * The Producer is not really closed but sent back into pool.
     */
    void close();

    /**
     * Shutdown this Producer, so it could not be used again.
     */
    public void shutDown();
}
