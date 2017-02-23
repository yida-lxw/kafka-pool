package com.gome.redline.consumer;

import com.gome.redline.config.DefaultConfigurable;
import com.gome.redline.consumer.handler.IKafkaConsumerHandler;
import com.gome.redline.decoder.IKafkaMessageDecoder;
import com.gome.redline.decoder.KafkaMessageStringDecoder;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Lanxiaowei at 2017/2/23 16:09
 * Default Kafka Consumer Implement
 */
public class DefaultKafkaConsumerImpl extends DefaultConfigurable implements IKafkaConsumer {
    private static final Logger log = LoggerFactory.getLogger(DefaultKafkaConsumerImpl.class);
    private String topics;
    private String groupId;
    private ExecutorService fixedThreadPool;
    private boolean hasInited = false;
    private KafkaConsumer<String, byte[]> consumer;
    private boolean isRunning;

    private IKafkaConsumerHandler consumerHandler;
    private IKafkaMessageDecoder decoder;
    private long poolMessageWaitTimeout;

    public DefaultKafkaConsumerImpl(String topics,IKafkaConsumerHandler consumerHandler) {
        this.topics = topics;
        this.consumerHandler = consumerHandler;
        this.decoder = new KafkaMessageStringDecoder();
        try {
            init(topics);
        } catch (IOException e) {
            log.error("Initialize the Kafka Consumer Instance occur exception:\n{}",e.getMessage());
        }
    }

    public DefaultKafkaConsumerImpl(String topics,IKafkaConsumerHandler consumerHandler,IKafkaMessageDecoder decoder) {
        this.topics = topics;
        this.consumerHandler = consumerHandler;
        this.decoder = decoder;
        try {
            init(topics);
        } catch (IOException e) {
            log.error("Initialize the Kafka Consumer Instance occur exception:\n{}",e.getMessage());
        }
    }

    public DefaultKafkaConsumerImpl(String topics,IKafkaConsumerHandler consumerHandler,long poolMessageWaitTimeout) {
        this.topics = topics;
        this.poolMessageWaitTimeout = poolMessageWaitTimeout;
        this.consumerHandler = consumerHandler;
        this.decoder = new KafkaMessageStringDecoder();
        try {
            init(topics);
        } catch (IOException e) {
            log.error("Initialize the Kafka Consumer Instance occur exception:\n{}",e.getMessage());
        }
    }

    public DefaultKafkaConsumerImpl(String topics,IKafkaConsumerHandler consumerHandler,IKafkaMessageDecoder decoder,long poolMessageWaitTimeout) {
        this.topics = topics;
        this.poolMessageWaitTimeout = poolMessageWaitTimeout;
        this.consumerHandler = consumerHandler;
        this.decoder = decoder;
        try {
            init(topics);
        } catch (IOException e) {
            log.error("Initialize the Kafka Consumer Instance occur exception:\n{}",e.getMessage());
        }
    }

    public DefaultKafkaConsumerImpl(String topics, String groupId,IKafkaConsumerHandler consumerHandler,long poolMessageWaitTimeout) {
        this.topics = topics;
        this.groupId = groupId;
        this.poolMessageWaitTimeout = poolMessageWaitTimeout;
        this.consumerHandler = consumerHandler;
        this.decoder = new KafkaMessageStringDecoder();
        this.getConfig().setGroupId(this.groupId);
        try {
            init(topics);
        } catch (IOException e) {
            log.error("Initialize the Kafka Consumer Instance occur exception:\n{}",e.getMessage());
        }
    }

    public DefaultKafkaConsumerImpl(String topics, String groupId,IKafkaConsumerHandler consumerHandler,IKafkaMessageDecoder decoder,long poolMessageWaitTimeout) {
        this.topics = topics;
        this.groupId = groupId;
        this.poolMessageWaitTimeout = poolMessageWaitTimeout;
        this.decoder = decoder;
        this.consumerHandler = consumerHandler;
        this.getConfig().setGroupId(this.groupId);
        try {
            init(topics);
        } catch (IOException e) {
            log.error("Initialize the Kafka Consumer Instance occur exception:\n{}",e.getMessage());
        }
    }

    private void init(String topics) throws IOException {
        if(this.hasInited){
            return;
        }
        int threadNum = this.getConfig().getConsumerProcessThreadNum();
        if(threadNum > 0){
            this.fixedThreadPool = Executors.newFixedThreadPool(threadNum);
        }
        this.consumer = new KafkaConsumer<String,byte[]>(this.getConfig().getKafkaConfig());
        this.hasInited = true;
    }


    public void subscribe(String topics) {
        this.consumer.subscribe(Arrays.asList(topics.split(",")));
    }

    public void subscribe(List<String> topicList) {
        this.consumer.subscribe(topicList);
    }

    public void consumer(final IKafkaConsumerHandler consumerHandler, ConsumerRecords<String, byte[]> records) {
        if(consumerHandler == null){
            throw new NullPointerException("processor = null makes no sense");
        }
        for (final ConsumerRecord<String, byte[]> record : records) {
            if(null == fixedThreadPool){
                this.consumerHandler.handle(record,this.decoder);
            } else{
                fixedThreadPool.execute(new Runnable() {
                    public void run() {
                        consumerHandler.handle(record,decoder);
                    }
                });
            }
        }
    }

    public void run() {
        isRunning = true;
        if(-1 == this.poolMessageWaitTimeout || this.poolMessageWaitTimeout <= 0L) {
            this.poolMessageWaitTimeout = this.getConfig().getPoolMessageWaitTimeout();
        }
        while (isRunning && !Thread.interrupted()) {
            ConsumerRecords<String, byte[]> records = this.consumer.poll(this.poolMessageWaitTimeout);
            if(null == records || records.isEmpty()) {
                log.info("no kafka message for consuming.");
                continue;
            }
            consumer(this.consumerHandler,records);
            consumer.commitAsync();
        }
        if(null != this.consumer) {
            this.consumer.close();
        }
    }

    public void close() {
        this.isRunning = false;
    }

    public String getTopics() {
        return topics;
    }

    public void setTopics(String topics) {
        this.topics = topics;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }
}
