package com.gome.redline.pool;

import com.gome.redline.config.DefaultConfigurable;
import com.gome.redline.pool.callable.IPoolOfferCallable;
import com.gome.redline.producer.IKafkaProducer;
import java.lang.reflect.ParameterizedType;
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
 * Created by Lanxiaowei at 2017/2/24 12:45
 * Kafka Producer Pool abstract implement
 */
public abstract class AbstractKafkaProducerPool<T extends IKafkaProducer> extends DefaultConfigurable implements IKafkaProducerPool<T> {
    private static final Logger log = LoggerFactory.getLogger(AbstractKafkaProducerPool.class);
    protected Semaphore freeProducer;
    protected LinkedBlockingQueue<IKafkaProducer> queue;
    protected String topic;
    protected String brokerStr;
    protected int poolSize;
    protected ExecutorService pool;
    protected ReadWriteLock closingLock;
    protected Class<T> producerClass;
    private static final int INIT_TIMEOUT_MIN = 2;
    private static final int DEFAULT_POOL_SIZE = 0;

    public AbstractKafkaProducerPool(int poolSize, String topic, String brokerStr) {
        this.closingLock = new ReentrantReadWriteLock();
        this.topic = topic;
        this.poolSize = poolSize <= 0 ? this.getConfig().getKafkaProducerPoolSize() : poolSize;
        this.freeProducer = new Semaphore(this.poolSize);
        this.queue = new LinkedBlockingQueue<IKafkaProducer>(this.poolSize);
        this.pool = Executors.newFixedThreadPool(this.poolSize);
        this.brokerStr = brokerStr;
        this.getConfig().setMetadataBrokerList(brokerStr);
        makeProducerClassType();
        init();
    }

    public AbstractKafkaProducerPool(int poolSize, String topic) {
        this(poolSize,topic,"");
    }

    public AbstractKafkaProducerPool(String topic) {
        this(DEFAULT_POOL_SIZE,topic,"");
    }

    /**
     * Kafka Pool initi
     */
    private void init() {
        List<Callable<Boolean>> taskList = new ArrayList<Callable<Boolean>>();
        final CountDownLatch count = new CountDownLatch(this.poolSize);
        for(int i = 0;i < this.poolSize; i++) {
            taskList.add(createPoolOfferCallable());
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
            if (!this.freeProducer.tryAcquire(waitTimeMillis, TimeUnit.MILLISECONDS)) {
                throw new RuntimeException("Timeout waiting for idle object in the pool.");
            }
        } catch (InterruptedException e) {
            throw new RuntimeException("Interrupted waiting for idle object in the pool .");
        }
        this.closingLock.readLock().lock();
        IKafkaProducer producer = null;
        try {
            producer = this.queue.poll(this.getConfig().getKafkaWaitingProducerTimeout(), TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            log.info("get Producer from the Kafka pool occur exception.");
        }
        if(null == producer) {
            log.error("take a Kafka Producer from the pool have been timeout.");
        }
        this.closingLock.readLock().unlock();
        return producer;
    }

    public IKafkaProducer getProducer() {
        boolean wait = true;
        try {
            while(wait) {
                if(this.freeProducer.tryAcquire(this.getConfig().getKafkaWaitingProducerTimeout(), TimeUnit.MILLISECONDS)) {
                    wait = false;
                    break;
                }
            }
        } catch (InterruptedException e) {
            throw new RuntimeException("Interrupted waiting for idle object in the pool .");
        }
        this.closingLock.readLock().lock();
        IKafkaProducer producer = null;
        if(!wait) {
            //假如一直获取不到Producer，则会一直阻塞直到获取到Producer为止
            producer = this.queue.poll();
        }
        this.closingLock.readLock().unlock();
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
        this.closingLock.writeLock().lock();
        List<Callable<Boolean>> taskList = new ArrayList<Callable<Boolean>>();
        int size = this.queue.size();
        final CountDownLatch count = new CountDownLatch(this.queue.size());
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
            this.pool.invokeAll(taskList);
            count.await(INIT_TIMEOUT_MIN, TimeUnit.MINUTES);
            this.pool.shutdownNow();
        } catch (InterruptedException e) {
            log.error("Failed to close the KafkaProducerPool", e);
        }
        this.closingLock.writeLock().unlock();
    }

    public String getTopic() {
        return this.topic;
    }

    public int getPoolSize() {
        return this.poolSize;
    }

    public void setPoolSize(int poolSize) {
        this.poolSize = poolSize;
    }

    public LinkedBlockingQueue<IKafkaProducer> getQueue() {
        return this.queue;
    }

    private void makeProducerClassType() {
        this.producerClass = (Class<T>) ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

    public Class<T> getProducerClass() {
        return this.producerClass;
    }

    /**
     * Create the specific Pool Offer Callable Implement.
     * @return
     */
    protected abstract IPoolOfferCallable createPoolOfferCallable();
}
