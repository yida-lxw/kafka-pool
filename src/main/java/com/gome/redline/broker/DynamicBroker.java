package com.gome.redline.broker;

import com.gome.redline.config.DefaultConfigurable;
import com.gome.redline.utils.json.FastJSONUtils;
import com.netflix.curator.framework.CuratorFramework;
import com.netflix.curator.framework.CuratorFrameworkFactory;
import com.netflix.curator.retry.RetryNTimes;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static sun.security.krb5.Confounder.intValue;

/**
 * Created by Lanxiaowei at 2017/2/22 20:00
 * DynamicBroker读取工具类
 */
public class DynamicBroker extends DefaultConfigurable {
    public static final Logger log = LoggerFactory.getLogger(DynamicBroker.class);

    private CuratorFramework curator;
    private String zkPath;
    private String topic;
    private static final int INTERVAL_IN_MS = 100;

    public DynamicBroker(String topic) {
        this.zkPath = this.config.getDefaultZkRoot();
        this.topic = topic;
        try {
            this.curator = CuratorFrameworkFactory.newClient(
                    this.config.getZkHosts(),
                    new RetryNTimes(Integer.MAX_VALUE, INTERVAL_IN_MS));
            this.curator.start();
        } catch (IOException ex)  {
            log.error("can't connect to zookeeper");
        }
    }

    /**
     * Get all partitions with their current leaders
     */
    public String getBrokerInfo() {
        String brokerStr = "";
        try {
            int numPartitionsForTopic = getNumPartitions();
            String brokerInfoPath = brokerPath();
            for (int partition = 0; partition < numPartitionsForTopic; partition++) {
                int leader = getLeaderFor(partition);
                String path = brokerInfoPath + "/" + leader;
                try {
                    byte[] hostPortData = this.curator.getData().forPath(path);
                    brokerStr = brokerStr + getBrokerHost(hostPortData);
                    if(partition!=numPartitionsForTopic-1) {
                        brokerStr += ",";
                    }
                } catch(org.apache.zookeeper.KeeperException.NoNodeException e) {
                    log.error("Node {} does not exist ", path);
                }
            }
            this.curator.close();
        } catch(Exception e) {
            throw new RuntimeException(e);
        }
        log.info("Read partition info from zookeeper: " + brokerStr);
        return brokerStr;
    }

    /**
     * return the count of all Partitions.
     * @return
     */
    private int getNumPartitions() {
        try {
            String topicBrokersPath = partitionPath();
            List<String> children = this.curator.getChildren().forPath(topicBrokersPath);
            return children.size();
        } catch(Exception e) {
            log.error("get the count of all Partitions from zookeeper occur exception.");
        }
        return -1;
    }

    public String partitionPath() {
        return this.zkPath + "/topics/" + this.topic + "/partitions";
    }

    public String brokerPath() {
        return this.zkPath + "/ids";
    }

    /**
     * get /brokers/topics/distributedTopic/partitions/1/state
     * { "controller_epoch":4, "isr":[ 1, 0 ], "leader":1, "leader_epoch":1, "version":1 }
     * @param partition
     * @return
     */
    private int getLeaderFor(long partition) {
        try {
            String topicBrokersPath = partitionPath();
            byte[] hostPortData = curator.getData().forPath(topicBrokersPath + "/" + partition + "/state" );
            Map<String, Object> map = FastJSONUtils.stringToMap(new String(hostPortData, "UTF-8"));
            Integer leader = ((Number) map.get("leader")).intValue();
            return leader;
        } catch (Exception e) {
            log.error("get leader from zookeeper occur exception.");
        }
        return -1;
    }

    public void close() {
        this.curator.close();
    }

    /**
     *
     * [zk: localhost:2181(CONNECTED) 56] get /brokers/ids/0
     * { "host":"localhost", "jmx_port":9999, "port":9092, "version":1 }
     *
     * @param contents
     * @return
     */
    private String getBrokerHost(byte[] contents) {
        try {
            Map<String, Object> map = FastJSONUtils.stringToMap(new String(contents, "UTF-8"));
            String host = map.get("host").toString();
            Integer port = Integer.valueOf(map.get("port").toString());
            return host + ":" + port;
        } catch (UnsupportedEncodingException e) {
            log.error("get broker host from zookeeper occur exception.");
        }
        return null;
    }
}
