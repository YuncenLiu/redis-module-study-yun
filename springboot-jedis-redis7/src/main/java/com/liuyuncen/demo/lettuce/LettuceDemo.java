package com.liuyuncen.demo.lettuce;

import com.liuyuncen.constants.RedisConstants;
import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisURI;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisCommands;

/**
 * @belongsProject: redis-module-study-yun
 * @belongsPackage: com.liuyuncen.demo.lettuce
 * @author: Xiang想
 * @createTime: 2024-08-02  16:22
 * @description: TODO
 * @version: 1.0
 */
public class LettuceDemo {
    public static void main(String[] args) {
        RedisURI uri = RedisURI.builder()
                .redis(RedisConstants.REDIS_IP)
                .withPort(RedisConstants.REDIS_PORT)
                .withAuthentication("default",RedisConstants.REDIS_PASSWORD)
                .build();
        // 创建连接客户端
        RedisClient redisClient = RedisClient.create(uri);
        StatefulRedisConnection<String, String> conn = redisClient.connect();

        RedisCommands<String, String> commands = conn.sync();

        commands.set("yun","yuncen");
        System.out.println("yun = " + commands.get("yun"));



        // 关闭资源
        conn.close();
        redisClient.shutdown();
    }
}
