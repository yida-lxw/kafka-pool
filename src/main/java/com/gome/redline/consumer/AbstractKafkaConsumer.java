package com.gome.redline.consumer;

import com.gome.redline.config.DefaultConfigurable;
import com.gome.redline.consumer.handler.IKafkaConsumerHandler;
import com.gome.redline.decoder.IKafkaMessageDecoder;
import com.gome.redline.utils.Constant;
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
public abstract class AbstractKafkaConsumer<T> extends DefaultConfigurable implements IKafkaConsumer<T> {
    private static final Logger log = LoggerFactory.getLogger(AbstractKafkaConsumer.class);
    protected String topics;
    protected String groupId;
    protected ExecutorService fixedThreadPool;
    protected boolean hasInited = false;
    protected KafkaConsumer<String, byte[]> consumer;
    protected boolean isRunning;
    protected Class<T> beanClass;

    protected IKafkaConsumerHandler consumerHandler;
    protected IKafkaMessageDecoder decoder;
    protected long poolMessageWaitTimeout;

    public AbstractKafkaConsumer(String topics) {
        this(topics,null,null,null,-1,null,null);
    }

    public AbstractKafkaConsumer(String topics,Class<T> beanClass) {
        this(topics,null,null,null,-1,null,beanClass);
    }

    public AbstractKafkaConsumer(String topics, String charset) {
        this(topics,null,null,null,-1,charset,null);
    }

    public AbstractKafkaConsumer(String topics, String charset,Class<T> beanClass) {
        this(topics,null,null,null,-1,charset,beanClass);
    }

    public AbstractKafkaConsumer(String topics, IKafkaConsumerHandler consumerHandler) {
        this(topics,null,consumerHandler,null,-1,null,null);
    }

    public AbstractKafkaConsumer(String topics, IKafkaConsumerHandler consumerHandler,Class<T> beanClass) {
        this(topics,null,consumerHandler,null,-1,null,beanClass);
    }

    public AbstractKafkaConsumer(String topics, IKafkaConsumerHandler consumerHandler, IKafkaMessageDecoder decoder) {
        this(topics,null,consumerHandler,decoder,-1,null,null);
    }

    public AbstractKafkaConsumer(String topics, IKafkaConsumerHandler consumerHandler,
                                 IKafkaMessageDecoder decoder,Class<T> beanClass) {
        this(topics,null,consumerHandler,decoder,-1,null,beanClass);
    }

    public AbstractKafkaConsumer(String topics, IKafkaConsumerHandler consumerHandler, long poolMessageWaitTimeout, String charset) {
        this(topics,null,consumerHandler,null,poolMessageWaitTimeout,charset,null);
    }

    public AbstractKafkaConsumer(String topics, IKafkaConsumerHandler consumerHandler,
                                 long poolMessageWaitTimeout, String charset,Class<T> beanClass) {
        this(topics,null,consumerHandler,null,poolMessageWaitTimeout,charset,beanClass);
    }

    public AbstractKafkaConsumer(String topics, IKafkaConsumerHandler consumerHandler, IKafkaMessageDecoder decoder, long poolMessageWaitTimeout) {
        this(topics,null,consumerHandler,decoder,poolMessageWaitTimeout,null,null);
    }

    public AbstractKafkaConsumer(String topics, IKafkaConsumerHandler consumerHandler,
                                 IKafkaMessageDecoder decoder, long poolMessageWaitTimeout,Class<T> beanClass) {
        this(topics,null,consumerHandler,decoder,poolMessageWaitTimeout,null,beanClass);
    }

    public AbstractKafkaConsumer(String topics, String groupId, IKafkaConsumerHandler consumerHandler, long poolMessageWaitTimeout, String charset) {
        this(topics,groupId,consumerHandler,null,poolMessageWaitTimeout,charset,null);
    }

    public AbstractKafkaConsumer(String topics, String groupId, IKafkaConsumerHandler consumerHandler,
                                 IKafkaMessageDecoder decoder, long poolMessageWaitTimeout, String charset,Class<T> beanClass) {
        this.topics = topics;
        this.groupId = groupId;
        this.beanClass = beanClass;
        this.poolMessageWaitTimeout = poolMessageWaitTimeout;
        this.decoder = decoder == null?
                createMessageDecoder((null == charset || "".equals(charset))?
                        Constant.DEFAULT_CHARSET : charset) : decoder;
        this.consumerHandler = consumerHandler == null?
                createConsumerHandler((null == charset || "".equals(charset))?
                        Constant.DEFAULT_CHARSET : charset) : consumerHandler;
        if(null != groupId && "".equals(groupId)) {
            this.getConfig().setGroupId(this.groupId);
        }
        if(poolMessageWaitTimeout >= 0) {
            this.getConfig().setPoolMessageWaitTimeout(poolMessageWaitTimeout);
        }
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

    protected abstract IKafkaConsumerHandler createConsumerHandler(String charset);

    protected abstract IKafkaMessageDecoder createMessageDecoder(String charset);

    protected IKafkaConsumerHandler createConsumerHandler() {
        return createConsumerHandler(Constant.DEFAULT_CHARSET);
    }

    protected IKafkaMessageDecoder createMessageDecoder() {
        return createMessageDecoder(Constant.DEFAULT_CHARSET);
    }

    public void setBeanClass(Class<T> beanClass) {
        this.beanClass = beanClass;
    }
}
