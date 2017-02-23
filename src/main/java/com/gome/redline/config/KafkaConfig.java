package com.gome.redline.config;

import java.util.Properties;

/**
 * Created by Lanxiaowei at 2017/2/22 19:11
 * Kafka配置对象
 */
public class KafkaConfig {
    private String kafkaServer;
    private String zkHosts;
    private String autoOffsetReset;
    private String metadataBrokerList;
    private String producerType;
    private int acks;
    private String keySerializer;
    private String valueSerializer;
    private String keyDeSerializer;
    private String valueDeSerializer;
    private int batchSize;
    private long lingerMS;
    private long bufferMemory;
    private String defaultZkRoot;
    private int kafkaProducerPoolSize;
    private long kafkaWaitingProducerTimeout;

    /****************************Kafka Consumer Config****************************************/
    private boolean consumerEnableAutoCommit;
    private long autoCommitInterval;
    private long sessionTimeout;
    private long poolMessageWaitTimeout;
    private int consumerProcessThreadNum;
    private String groupId;
    /****************************Kafka Consumer Config****************************************/

    public static final String DEFAULT_ZK_ROOT = "/brokers";
    public static final int DEFAULT_KAFKA_PRODUCER_POOL_SIZE = 100;
    public static final long KAFKA_WAITING_PRODUCER_TIMEOUT_MS = 6000L;

    public static final boolean KAFKA_CONSUMER_ENABLE_AUTO_COMMIT = true;
    public static final long KAFKA_CONSUMER_AUTO_COMMIT_INTERVAL = 1000L;
    public static final long KAFKA_CONSUMER_SESSION_TIMEOUT = 30000L;
    public static final long KAFKA_CONSUMER_POOL_MESSAGE_WAIT_TIMEOUT = 1000L;
    public static final int KAFKA_CONSUMER_PROCESS_THREAD_NUM = 10;
    public static final String KAFKA_CONSUMER_GROUP_ID = "test-group";

    private KafkaConfig () {
        this.initialize();
    }

    private static class SingletonHolder {
        private static final KafkaConfig INSTANCE = new KafkaConfig();
    }

    public static final KafkaConfig getInstance() {
        return SingletonHolder.INSTANCE;
    }

    /**
     * @Author Lanxiaowei
     * @Title: initialize
     * @Description: Kafka配置初始化
     * @return void
     * @throws
     */
    private void initialize() {
        this.kafkaServer = KafkaConfigurations.getStringProperty("bootstrap.servers");
        this.zkHosts = KafkaConfigurations.getStringProperty("zk.hosts");
        this.autoOffsetReset = KafkaConfigurations.getStringProperty("auto.offset.reset","latest");
        this.metadataBrokerList = KafkaConfigurations.getStringProperty("metadata.broker.list");
        this.producerType = KafkaConfigurations.getStringProperty("producer.type","async");
        this.acks = KafkaConfigurations.getIntProperty("acks",-1);
        this.keySerializer = KafkaConfigurations.getStringProperty("key.serializer","org.apache.kafka.common.serialization.StringSerializer");
        this.valueSerializer = KafkaConfigurations.getStringProperty("value.serializer","org.apache.kafka.common.serialization.ByteArraySerializer");
        this.keyDeSerializer = KafkaConfigurations.getStringProperty("key.deserializer","org.apache.kafka.common.serialization.StringDeserializer");
        this.valueDeSerializer = KafkaConfigurations.getStringProperty("value.deserializer","org.apache.kafka.common.serialization.ByteArrayDeserializer");
        this.batchSize = KafkaConfigurations.getIntProperty("batch.size",100);
        this.lingerMS = KafkaConfigurations.getLongProperty("linger.ms",1L);
        this.bufferMemory = KafkaConfigurations.getLongProperty("buffer.memory",33554432L);
        this.defaultZkRoot = KafkaConfigurations.getStringProperty("default.zk.root",DEFAULT_ZK_ROOT);
        this.kafkaProducerPoolSize = KafkaConfigurations.getIntProperty("kafka.producer.pool.size",DEFAULT_KAFKA_PRODUCER_POOL_SIZE);
        this.kafkaWaitingProducerTimeout = KafkaConfigurations.getLongProperty("kafka.waiting.producer.timeout",KAFKA_WAITING_PRODUCER_TIMEOUT_MS);

        this.consumerEnableAutoCommit = KafkaConfigurations.getBooleanProperty("enable.auto.commit",KAFKA_CONSUMER_ENABLE_AUTO_COMMIT);
        this.autoCommitInterval = KafkaConfigurations.getLongProperty("auto.commit.interval.ms",KAFKA_CONSUMER_AUTO_COMMIT_INTERVAL);
        this.sessionTimeout = KafkaConfigurations.getLongProperty("session.timeout.ms",KAFKA_CONSUMER_SESSION_TIMEOUT);
        this.poolMessageWaitTimeout = KafkaConfigurations.getLongProperty("pool.message.wait.timeout",KAFKA_CONSUMER_POOL_MESSAGE_WAIT_TIMEOUT);
        this.consumerProcessThreadNum = KafkaConfigurations.getIntProperty("consumer.process.thread.num",KAFKA_CONSUMER_PROCESS_THREAD_NUM);
        this.groupId = KafkaConfigurations.getStringProperty("group.id",KAFKA_CONSUMER_GROUP_ID);
    }

    /**
     * 验证Kafka配置信息
     * @throws Exception
     */
    public void validate() throws Exception {
        if (null == kafkaServer || "".equals(kafkaServer)) {
            throw new Exception("KafkaServer is not set in the KafkaConfig.");
        }
        if (null == zkHosts || "".equals(zkHosts)) {
            throw new Exception("Zookeeper Host is not set in the KafkaConfig.");
        }
    }

    public Properties getKafkaConfig() {
        return KafkaConfigurations.getProperties();
    }

    public String getKafkaServer() {
        return kafkaServer;
    }

    public void setKafkaServer(String kafkaServer) {
        this.kafkaServer = kafkaServer;
    }

    public String getZkHosts() {
        return zkHosts;
    }

    public void setZkHosts(String zkHosts) {
        this.zkHosts = zkHosts;
    }

    public String getAutoOffsetReset() {
        return autoOffsetReset;
    }

    public void setAutoOffsetReset(String autoOffsetReset) {
        this.autoOffsetReset = autoOffsetReset;
    }

    public String getMetadataBrokerList() {
        return metadataBrokerList;
    }

    public void setMetadataBrokerList(String metadataBrokerList) {
        this.metadataBrokerList = metadataBrokerList;
        KafkaConfigurations.getProperties().put("metadata.broker.list",metadataBrokerList);
    }

    public String getProducerType() {
        return producerType;
    }

    public void setProducerType(String producerType) {
        this.producerType = producerType;
    }

    public int getAcks() {
        return acks;
    }

    public void setAcks(int acks) {
        this.acks = acks;
    }

    public String getKeySerializer() {
        return keySerializer;
    }

    public void setKeySerializer(String keySerializer) {
        this.keySerializer = keySerializer;
    }

    public String getValueSerializer() {
        return valueSerializer;
    }

    public void setValueSerializer(String valueSerializer) {
        this.valueSerializer = valueSerializer;
    }

    public int getBatchSize() {
        return batchSize;
    }

    public void setBatchSize(int batchSize) {
        this.batchSize = batchSize;
    }

    public long getLingerMS() {
        return lingerMS;
    }

    public void setLingerMS(long lingerMS) {
        this.lingerMS = lingerMS;
    }

    public long getBufferMemory() {
        return bufferMemory;
    }

    public void setBufferMemory(long bufferMemory) {
        this.bufferMemory = bufferMemory;
    }

    public String getDefaultZkRoot() {
        return defaultZkRoot;
    }

    public void setDefaultZkRoot(String defaultZkRoot) {
        this.defaultZkRoot = defaultZkRoot;
    }

    public int getKafkaProducerPoolSize() {
        return kafkaProducerPoolSize;
    }

    public void setKafkaProducerPoolSize(int kafkaProducerPoolSize) {
        this.kafkaProducerPoolSize = kafkaProducerPoolSize;
    }

    public long getKafkaWaitingProducerTimeout() {
        return kafkaWaitingProducerTimeout;
    }

    public void setKafkaWaitingProducerTimeout(long kafkaWaitingProducerTimeout) {
        this.kafkaWaitingProducerTimeout = kafkaWaitingProducerTimeout;
    }

    public boolean isConsumerEnableAutoCommit() {
        return consumerEnableAutoCommit;
    }

    public void setConsumerEnableAutoCommit(boolean consumerEnableAutoCommit) {
        this.consumerEnableAutoCommit = consumerEnableAutoCommit;
    }

    public long getAutoCommitInterval() {
        return autoCommitInterval;
    }

    public void setAutoCommitInterval(long autoCommitInterval) {
        this.autoCommitInterval = autoCommitInterval;
    }

    public long getSessionTimeout() {
        return sessionTimeout;
    }

    public void setSessionTimeout(long sessionTimeout) {
        this.sessionTimeout = sessionTimeout;
    }

    public long getPoolMessageWaitTimeout() {
        return poolMessageWaitTimeout;
    }

    public void setPoolMessageWaitTimeout(long poolMessageWaitTimeout) {
        this.poolMessageWaitTimeout = poolMessageWaitTimeout;
    }

    public int getConsumerProcessThreadNum() {
        return consumerProcessThreadNum;
    }

    public void setConsumerProcessThreadNum(int consumerProcessThreadNum) {
        this.consumerProcessThreadNum = consumerProcessThreadNum;
    }

    public String getKeyDeSerializer() {
        return keyDeSerializer;
    }

    public void setKeyDeSerializer(String keyDeSerializer) {
        this.keyDeSerializer = keyDeSerializer;
    }

    public String getValueDeSerializer() {
        return valueDeSerializer;
    }

    public void setValueDeSerializer(String valueDeSerializer) {
        this.valueDeSerializer = valueDeSerializer;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
        KafkaConfigurations.getProperties().put("group.id",this.groupId);
    }
}
