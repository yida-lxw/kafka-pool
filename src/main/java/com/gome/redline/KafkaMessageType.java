package com.gome.redline;

/**
 * Created by Lanxiaowei at 2017/2/24 14:15
 * Kafka Message Type Enum
 */
public enum KafkaMessageType {
    /** 字符串类型Kafka消息 */
    String,
    /** Map<String,Object>类型Kafka消息 */
    Map,
    /** JavaBean类型Kafka消息 */
    Bean
}
