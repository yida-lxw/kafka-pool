package com.gome.redline.pool;

import com.gome.redline.broker.DynamicBroker;
import com.gome.redline.config.DefaultConfigurable;
import com.gome.redline.producer.IKafkaProducer;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Lanxiaowei at 2017/2/22 20:18
 * Kafka Producer Pool
 */
public class KafkaProducerPool extends DefaultConfigurable {
    private static final Logger log = LoggerFactory.getLogger(KafkaProducerPool.class);
    private Semaphore freeProducer;
    private LinkedBlockingQueue<IKafkaProducer> queue;
    private String topic;
    private String brokerStr;
    private int poolSize;
    private ExecutorService pool;
    private ReadWriteLock closingLock;
    private static final int INIT_TIMEOUT_MIN = 2;

    public KafkaProducerPool(int poolSize, String topic, String brokerStr) {
        this.closingLock = new ReentrantReadWriteLock();
        this.topic = topic;
        this.poolSize = poolSize;
        this.freeProducer = new Semaphore(poolSize);
        this.queue = new LinkedBlockingQueue<IKafkaProducer>(poolSize);
        this.pool = Executors.newFixedThreadPool(poolSize);
        this.brokerStr = brokerStr;
        this.getConfig().setMetadataBrokerList(brokerStr);
        init();
    }

    public KafkaProducerPool(int poolSize, String topic) {
        this.closingLock = new ReentrantReadWriteLock();
        this.topic = topic;
        this.poolSize = poolSize;
        this.freeProducer = new Semaphore(poolSize);
        this.queue = new LinkedBlockingQueue<IKafkaProducer>(poolSize);
        this.pool = Executors.newFixedThreadPool(poolSize);
        DynamicBroker reader = new DynamicBroker(this.topic);
        this.brokerStr = reader.getBrokerInfo();
        this.getConfig().setMetadataBrokerList(brokerStr);
        init();
    }

    public KafkaProducerPool(String topic) {
        this.closingLock = new ReentrantReadWriteLock();
        this.topic = topic;
        this.poolSize = this.getConfig().getKafkaProducerPoolSize();
        this.freeProducer = new Semaphore(poolSize);
        this.queue = new LinkedBlockingQueue<IKafkaProducer>(poolSize);
        this.pool = Executors.newFixedThreadPool(poolSize);
        DynamicBroker reader = new DynamicBroker(this.topic);
        this.brokerStr = reader.getBrokerInfo();
        this.getConfig().setMetadataBrokerList(brokerStr);
        init();
    }

    /**
     * Kafka Pool initi
     */
    private void init() {
        List<Callable<Boolean>> taskList = new ArrayList<Callable<Boolean>>();
        final CountDownLatch count = new CountDownLatch(poolSize);
        for(int i = 0;i < poolSize; i++) {
            taskList.add(new PoolOfferCallable(this));
            count.countDown();
        }
        try {
            this.pool.invokeAll(taskList);
            count.await(INIT_TIMEOUT_MIN, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            log.error("Failed to init the KafkaProducerPool", e);
        }
    }

    /**
     * Get a Producer from the pool within the given timeout
     * @param waitTimeMillis
     * how long should it wait for getting the Producer instance
     * @return a Kafka Producer instance
     */
    public IKafkaProducer getProducer(long waitTimeMillis) {
        try {
            if (!freeProducer.tryAcquire(waitTimeMillis, TimeUnit.MILLISECONDS)) {
                throw new RuntimeException("Timeout waiting for idle object in the pool.");
            }
        } catch (InterruptedException e) {
            throw new RuntimeException("Interrupted waiting for idle object in the pool .");
        }
        closingLock.readLock().lock();
        IKafkaProducer producer = null;
        try {
            producer = queue.poll(this.getConfig().getKafkaWaitingProducerTimeout(), TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            log.info("get Producer from the Kafka pool occur exception.");
        }
        if(null == producer) {
            /*producer = new KafkaStringProducer(this);
            if(producer != null) {
                log.info("Add a new Kafka Producer to the pool.");
                queue.offer(producer);
            }*/
            log.error("take a Kafka Producer from the pool have been timeout.");
        }
        closingLock.readLock().unlock();
        return producer;
    }

    public IKafkaProducer getProducer() {
        boolean wait = true;
        try {
            while(wait) {
                if(freeProducer.tryAcquire(this.getConfig().getKafkaWaitingProducerTimeout(), TimeUnit.MILLISECONDS)) {
                    wait = false;
                    break;
                }
            }
        } catch (InterruptedException e) {
            throw new RuntimeException("Interrupted waiting for idle object in the pool .");
        }
        closingLock.readLock().lock();
        IKafkaProducer producer = null;
        if(!wait) {
            //假如一直获取不到Producer，则会一直阻塞直到获取到Producer为止
            producer = queue.poll();
        }
        closingLock.readLock().unlock();
        return producer;
    }

    /**
     * return Kafka Producer to the pool
     * @param producer
     */
    public void returnProducer(IKafkaProducer producer) {
        if(this.queue.contains(producer)) {
            return;
        }
        this.queue.offer(producer);
        this.freeProducer.release();
    }

    /**
     * Close the ProducerPool
     */
    public void close() {
        closingLock.writeLock().lock();
        List<Callable<Boolean>> taskList = new ArrayList<Callable<Boolean>>();
        int size = queue.size();
        final CountDownLatch count = new CountDownLatch(queue.size());
        for(int i=0;i<size;i++) {
            taskList.add(new Callable<Boolean>() {
                public Boolean call() throws Exception {
                    IKafkaProducer producer = queue.poll();
                    if (null != producer) {
                        producer.shutDown();
                        count.countDown();
                    }
                    return true;
                }
            });
        }
        try {
            pool.invokeAll(taskList);
            count.await(INIT_TIMEOUT_MIN, TimeUnit.MINUTES);
            pool.shutdownNow();
        } catch (InterruptedException e) {
            log.error("Failed to close the KafkaProducerPool", e);
        }
        closingLock.writeLock().unlock();
    }

    public String getTopic() {
        return topic;
    }

    public int getPoolSize() {
        return poolSize;
    }

    public void setPoolSize(int poolSize) {
        this.poolSize = poolSize;
    }

    public LinkedBlockingQueue<IKafkaProducer> getQueue() {
        return queue;
    }
}
