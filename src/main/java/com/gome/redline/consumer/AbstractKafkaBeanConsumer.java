package com.gome.redline.consumer;

import com.gome.redline.consumer.handler.IKafkaConsumerHandler;
import com.gome.redline.decoder.IKafkaMessageDecoder;
import com.gome.redline.model.KafkaMessageSendable;
import com.gome.redline.utils.Constant;
import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.util.Arrays;
import java.util.List;
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
public abstract class AbstractKafkaBeanConsumer<T extends KafkaMessageSendable> extends AbstractKafkaConsumer<T> {
    private static final Logger log = LoggerFactory.getLogger(AbstractKafkaBeanConsumer.class);

    public AbstractKafkaBeanConsumer(String topics) {
        this(topics, null, null, null,-1, null);
    }

    public AbstractKafkaBeanConsumer(String topics,Class<T> beanClass) {
        this(topics, null, null, null,-1, null,beanClass);
    }

    public AbstractKafkaBeanConsumer(String topics, String charset) {
        this(topics, null, null, null,-1, charset);
    }

    public AbstractKafkaBeanConsumer(String topics, String charset,Class<T> beanClass) {
        this(topics, null, null, null,-1, charset,beanClass);
    }

    public AbstractKafkaBeanConsumer(String topics, IKafkaConsumerHandler consumerHandler) {
        this(topics, null, consumerHandler, null,-1, null);
    }

    public AbstractKafkaBeanConsumer(String topics, IKafkaConsumerHandler consumerHandler,Class<T> beanClass) {
        this(topics, null, consumerHandler, null,-1, null,beanClass);
    }

    public AbstractKafkaBeanConsumer(String topics, IKafkaConsumerHandler consumerHandler, IKafkaMessageDecoder decoder) {
        this(topics, null, consumerHandler, decoder,-1, null);
    }

    public AbstractKafkaBeanConsumer(String topics, IKafkaConsumerHandler consumerHandler,
                                     IKafkaMessageDecoder decoder,Class<T> beanClass) {
        this(topics, null, consumerHandler, decoder,-1, null,beanClass);
    }

    public AbstractKafkaBeanConsumer(String topics, IKafkaConsumerHandler consumerHandler, long poolMessageWaitTimeout, String charset) {
        this(topics, null, consumerHandler, null,poolMessageWaitTimeout, charset);
    }

    public AbstractKafkaBeanConsumer(String topics, IKafkaConsumerHandler consumerHandler,
                                     long poolMessageWaitTimeout, String charset,Class<T> beanClass) {
        this(topics, null, consumerHandler, null,poolMessageWaitTimeout, charset,beanClass);
    }

    public AbstractKafkaBeanConsumer(String topics, IKafkaConsumerHandler consumerHandler, IKafkaMessageDecoder decoder, long poolMessageWaitTimeout) {
        this(topics, null, consumerHandler, decoder,poolMessageWaitTimeout, null);
    }

    public AbstractKafkaBeanConsumer(String topics, IKafkaConsumerHandler consumerHandler,
                                     IKafkaMessageDecoder decoder, long poolMessageWaitTimeout,Class<T> beanClass) {
        this(topics, null, consumerHandler, decoder,poolMessageWaitTimeout, null,beanClass);
    }

    public AbstractKafkaBeanConsumer(String topics, String groupId, IKafkaConsumerHandler consumerHandler, long poolMessageWaitTimeout, String charset) {
        this(topics, groupId, consumerHandler, null,poolMessageWaitTimeout, charset);
    }

    public AbstractKafkaBeanConsumer(String topics, String groupId, IKafkaConsumerHandler consumerHandler,
                                     long poolMessageWaitTimeout, String charset,Class<T> beanClass) {
        this(topics, groupId, consumerHandler, null,poolMessageWaitTimeout, charset,beanClass);
    }

    public AbstractKafkaBeanConsumer(String topics, String groupId, IKafkaConsumerHandler consumerHandler,
                                     IKafkaMessageDecoder decoder, long poolMessageWaitTimeout, String charset) {
        this(topics, groupId, consumerHandler, decoder, poolMessageWaitTimeout, charset,null);
    }

    public AbstractKafkaBeanConsumer(String topics, String groupId, IKafkaConsumerHandler consumerHandler,
                                     IKafkaMessageDecoder decoder, long poolMessageWaitTimeout, String charset,Class<T> beanClass) {
        super(topics, groupId, consumerHandler, decoder, poolMessageWaitTimeout, charset,beanClass);
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
            if(null == fixedThreadPool) {
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

    protected abstract IKafkaConsumerHandler createConsumerHandler(String charset);

    protected abstract IKafkaMessageDecoder createMessageDecoder(String charset);

    protected IKafkaConsumerHandler createConsumerHandler() {
        return createConsumerHandler(Constant.DEFAULT_CHARSET);
    }

    protected IKafkaMessageDecoder createMessageDecoder() {
        return createMessageDecoder(Constant.DEFAULT_CHARSET);
    }
}
