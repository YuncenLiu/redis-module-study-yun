package com.liuyuncen.utils;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * @belongsProject: redis-module-study-yun
 * @belongsPackage: com.liuyuncen.utils
 * @author: Xiang想
 * @createTime: 2024-08-06  14:24
 * @description: TODO
 * @version: 1.0
 */
public class RedisUtils {
    public static final String REDIS_IP_ADDR = "192.168.58.10";

    public static final String REDIS_PWD = "123456";

    public static JedisPool jedisPool;

    static {
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxTotal(20);
        jedisPoolConfig.setMaxIdle(10);
        jedisPool = new JedisPool(jedisPoolConfig, REDIS_IP_ADDR, 6379, 10000, REDIS_PWD);
    }

    public static Jedis getJedis() throws Exception {
        if (null != jedisPool) {
            return jedisPool.getResource();
        }
        throw new Exception("Jedispoll is not ok");
    }
}
