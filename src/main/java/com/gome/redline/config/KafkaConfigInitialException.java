package com.gome.redline.config;

/**
 * Created by Lanxiaowei at 2017/2/22 18:41
 * Kafka配置文件初始化异常
 */
public class KafkaConfigInitialException extends RuntimeException {
    public KafkaConfigInitialException() {
        super();
    }

    public KafkaConfigInitialException(String message) {
        super(message);
    }

    public KafkaConfigInitialException(String message, Throwable cause) {
        super(message, cause);
    }
}
