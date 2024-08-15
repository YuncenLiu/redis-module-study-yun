package com.liuyuncen.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.redisson.RedissonMultiLock;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

/**
 * @belongsProject: redis-module-study-yun
 * @belongsPackage: com.liuyuncen.controller
 * @author: Xiang想
 * @createTime: 2024-08-15  11:24
 * @description: TODO
 * @version: 1.0
 */
@RestController
@RequestMapping("/redisson")
@Slf4j
@Api(tags = "redisLock 实现")
public class RedLockController {
    public static final String CACHE_KEY_REDLOCK = "REDLOCK";

    @Autowired
    RedissonClient redissonClient1;
    @Autowired
    RedissonClient redissonClient2;
    @Autowired
    RedissonClient redissonClient3;

    @ApiOperation("1、获取多重锁初次体验")
    @GetMapping("/multilock")
    public String getMultiLock() {
        String taskThreadName = Thread.currentThread().getName() + " " + Thread.currentThread().getId();
        RLock lock1 = redissonClient1.getLock(CACHE_KEY_REDLOCK);
        RLock lock2 = redissonClient2.getLock(CACHE_KEY_REDLOCK);
        RLock lock3 = redissonClient3.getLock(CACHE_KEY_REDLOCK);

        RedissonMultiLock redLock = new RedissonMultiLock(lock1, lock2, lock3);
        redLock.lock();
        try {
            log.info("come in biz multilock:{}", taskThreadName);
            TimeUnit.SECONDS.sleep(30);
            log.info("task is over multilock:{}", taskThreadName);

        } catch (Exception e) {
            e.printStackTrace();
            log.error("multilock exception:{}", e.getCause() + "\t" + e.getMessage());
        } finally {
            redLock.unlock();
            log.info("释放分布式锁成功 key:{}, 当前线程:{}", CACHE_KEY_REDLOCK, taskThreadName);
        }


        return "multilock task is over: " + taskThreadName;
    }

}
