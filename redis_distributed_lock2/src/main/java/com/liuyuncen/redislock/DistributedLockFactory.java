package com.liuyuncen.redislock;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.locks.Lock;

/**
 * 功能描述： 集成所有分布式锁
 *
 * @author: Xiang
 * @date: 2024年08月12日 23:59:22
 * @Description:
 */
@Component
public class DistributedLockFactory {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    private String lockName;
    public Lock getDistributedLock(String lockType){
        if (null == lockType) {
            return null;
        }
        if (lockType.equalsIgnoreCase("REDIS")) {
            this.lockName = "rdl";
            return new RedisDistributedLock(stringRedisTemplate, lockName);
        } else if (lockType.equalsIgnoreCase("ZOOKEEPER")){
            this.lockName = "zkl";
            // TODO zookeeper 分布式锁
            return null;
        } else if (lockType.equalsIgnoreCase("MYSQL")){
            // TODO MySQL 分布式锁
            return null;
        }
        return null;
    }
}
