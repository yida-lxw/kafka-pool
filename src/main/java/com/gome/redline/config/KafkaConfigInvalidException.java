package com.gome.redline.config;

/**
 * Created by Lanxiaowei at 2017/2/24 14:10
 * Kafka配置文件验证失败异常
 */
public class KafkaConfigInvalidException extends RuntimeException {
    public KafkaConfigInvalidException() {
        super();
    }

    public KafkaConfigInvalidException(String message) {
        super(message);
    }

    public KafkaConfigInvalidException(String message, Throwable cause) {
        super(message, cause);
    }
}
