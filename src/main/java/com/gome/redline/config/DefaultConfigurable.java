package com.gome.redline.config;

/**
 * Created by Lanxiaowei at 2017/2/22 19:45
 * Kafka可配置接口默认实现
 */
public class DefaultConfigurable implements Configurable {
    protected KafkaConfig config;

    public DefaultConfigurable() {
        initConfig();
    }

    /**
     * @Author: Lanxiaowei
     * @Title: initConfig
     * @Description: 初始化Kafka配置对象
     * @return void
     * @throws
     */
    public KafkaConfig initConfig() {
        this.config = KafkaConfig.getInstance();
        return this.config;
    }

    public KafkaConfig getConfig() {
        return config;
    }

    public void setConfig(KafkaConfig config) {
        this.config = config;
    }
}
