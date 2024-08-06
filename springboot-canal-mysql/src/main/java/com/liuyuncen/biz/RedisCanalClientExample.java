package com.liuyuncen.biz;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.otter.canal.client.CanalConnector;
import com.alibaba.otter.canal.client.CanalConnectors;
import com.alibaba.otter.canal.protocol.CanalEntry.*;
import com.alibaba.otter.canal.protocol.Message;
import com.liuyuncen.utils.RedisUtils;
import redis.clients.jedis.Jedis;

import java.net.InetSocketAddress;
import java.util.List;
import java.util.UUID;

/**
 * @belongsProject: redis-module-study-yun
 * @belongsPackage: com.liuyuncen.biz
 * @author: Xiang想
 * @createTime: 2024-08-06  14:24
 * @description: TODO
 * @version: 1.0
 */
public class RedisCanalClientExample {

    public static final Integer _60SECONDS = 60;

    public static void redisInsert(List<Column> columns) {

        JSONObject jsonObject = new JSONObject();
        for (Column column : columns) {
            System.out.println(column.getName() + ": " + column.getValue() + " insert = " + column.getUpdated());
            jsonObject.put(column.getName(), column.getValue());
        }

        if (columns.size() > 0) {
            try (Jedis jedis = RedisUtils.getJedis()) {
                jedis.set(columns.get(0).getValue(), jsonObject.toJSONString());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void redisDelete(List<Column> columns) {
        JSONObject jsonObject = new JSONObject();
        for (Column column : columns) {
            System.out.println(column.getName() + ": " + column.getValue() + " delete = " + column.getUpdated());
            jsonObject.put(column.getName(), column.getValue());
        }

        if (columns.size() > 0) {
            try (Jedis jedis = RedisUtils.getJedis()) {
                jedis.del(columns.get(0).getValue());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void redisUpdate(List<Column> columns) {

        JSONObject jsonObject = new JSONObject();
        for (Column column : columns) {
            System.out.println(column.getName() + ": " + column.getValue() + " update = " + column.getUpdated());
            jsonObject.put(column.getName(), column.getValue());
        }

        if (columns.size() > 0) {
            try (Jedis jedis = RedisUtils.getJedis()) {
                jedis.set(columns.get(0).getValue(), jsonObject.toJSONString());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void printEntry(List<Entry> entrys) {
        for (Entry entry : entrys) {
            if (entry.getEntryType() == EntryType.TRANSACTIONBEGIN || entry.getEntryType() == EntryType.TRANSACTIONEND) {
                continue;
            }

            RowChange rowChage = null;
            try {
                rowChage = RowChange.parseFrom(entry.getStoreValue());
            } catch (Exception e) {
                throw new RuntimeException("ERROR ## parser of eromanga-event has an error , data:" + entry.toString(),
                        e);
            }

            EventType eventType = rowChage.getEventType();
            System.out.println(String.format("================&gt; binlog[%s:%s] , name[%s,%s] , eventType : %s",
                    entry.getHeader().getLogfileName(), entry.getHeader().getLogfileOffset(),
                    entry.getHeader().getSchemaName(), entry.getHeader().getTableName(),
                    eventType));

            for (RowData rowData : rowChage.getRowDatasList()) {
                if (eventType == EventType.DELETE) {
                    redisDelete(rowData.getBeforeColumnsList());
                } else if (eventType == EventType.INSERT) {
                    redisInsert(rowData.getAfterColumnsList());
                } else {
                    System.out.println("-------> before");
                    redisUpdate(rowData.getBeforeColumnsList());
                    System.out.println("-------> after");
                }
            }
        }
    }

    public static void main(String[] args) {
        // 创建链接
        CanalConnector connector = CanalConnectors.newSingleConnector(new InetSocketAddress("192.168.58.10",
                11111), "example", "", "");
        int batchSize = 1000;
        int emptyCount = 0;

        // 这里使用了 try-with-resources
        /*
         * JDK 1.7 添加了 try-with-resources ，保证最后每个资源都关闭
         * 任何实现了 java.lang.AutoCloseable 和 java.io.Closeable 的对象都可以使用 try-with-resources 实现异常处理和资源关闭
         */
        try {
            connector.connect();
            // 监听当前库的所有表
            // connector.subscribe(".*\\..*");

            // 正常情况一个应用监控一张表就好，监控的越多，canal负担压力越大
            connector.subscribe("mysql_canal.t_user");
            connector.rollback();
            int totalEmptyCount = 10 * _60SECONDS;
            while (emptyCount < totalEmptyCount) {
                System.out.println("我是canal，每秒监听一次：" + UUID.randomUUID().toString());
                Message message = connector.getWithoutAck(batchSize); // 获取指定数量的数据
                long batchId = message.getId();
                int size = message.getEntries().size();
                if (batchId == -1 || size == 0) {
                    // MySQL数据没有变动
                    emptyCount++;
                    System.out.println("empty count : " + emptyCount);
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                    }
                } else {
                    emptyCount = 0;
                    // System.out.printf("message[batchId=%s,size=%s] \n", batchId, size);
                    printEntry(message.getEntries());
                }

                connector.ack(batchId); // 提交确认
                // connector.rollback(batchId); // 处理失败, 回滚数据
            }

            System.out.println("empty too many times, exit");
        } finally {
            connector.disconnect();
        }
    }
}
