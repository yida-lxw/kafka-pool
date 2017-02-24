package com.gome.redline.config;

/**
 * Created by Lanxiaowei at 2017/2/22 19:44
 * Kafka可配置接口
 */
public interface Configurable {
    /**
     * Kafka配置初始化
     * @return
     */
    public KafkaConfig initConfig();

    /**
     * 获取Kafka配置对象
     * @return
     */
    KafkaConfig getConfig();
}
