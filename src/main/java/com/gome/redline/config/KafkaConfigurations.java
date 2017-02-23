package com.gome.redline.config;

import com.gome.redline.utils.common.PropertiesUtils;
import java.util.Properties;

/**
 * Created by Lanxiaowei at 2017/2/22 18:35
 * Kafka配置文件读取
 */
public class KafkaConfigurations {
    private static final boolean DEBUG = true;
    private static Properties prop = new Properties();

    static {
        // 读取Kafka配置文件
        prop = PropertiesUtils.loadPropertyFile("kafka_pool.properties", DEBUG);
        if (null == prop) {
            throw new KafkaConfigInitialException("An exception occurs as initialize the kafka configuration file.");
        }
    }

    public static Properties getProperties() {
        return prop;
    }

    /**
     * 根据key读取字符串类型的配置项
     * @param key
     * @param defaultValue  默认值
     * @return
     */
    public static String getStringProperty(String key, String defaultValue) {
        return PropertiesUtils.getStringProperty(prop,key,defaultValue);
    }

    public static String getStringProperty(String key) {
        return getStringProperty(key,null);
    }

    public static int getIntProperty(String key, int defaultValue) {
        return PropertiesUtils.getIntProperty(prop,key,defaultValue);
    }

    public static int getIntProperty(String key) {
        return getIntProperty(key,0);
    }

    public static short getShortProperty(String key, short defaultValue) {
        return PropertiesUtils.getShortProperty(prop,key,defaultValue);
    }

    public static short getShortProperty(String key) {
        return getShortProperty(key,(short)0);
    }

    public static long getLongProperty(String key, long defaultValue) {
        return PropertiesUtils.getLongProperty(prop,key,defaultValue);
    }

    public static long getLongProperty(String key) {
        return getLongProperty(key,0L);
    }

    public static float getFloatProperty(String key, float defaultValue) {
        return PropertiesUtils.getFloatProperty(prop,key,defaultValue);
    }

    public static float getFloatProperty(String key) {
        return getFloatProperty(key,0F);
    }

    public static double getDoubleProperty(String key, double defaultValue) {
        return PropertiesUtils.getDoubleProperty(prop,key,defaultValue);
    }

    public static double getDoubleProperty(String key) {
        return getDoubleProperty(key,0D);
    }

    public static boolean getBooleanProperty(String key, boolean defaultValue) {
        return PropertiesUtils.getBooleanProperty(prop,key,defaultValue);
    }

    public static boolean getBooleanProperty(String key) {
        return getBooleanProperty(key,false);
    }
}
