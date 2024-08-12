package com.liuyuncen.redislock;

import cn.hutool.core.util.IdUtil;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;

import java.util.Collections;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

/**
 * 功能描述： 自研分布式锁，实现 Lock 接口
 *
 * @author: Xiang
 * @date: 2024年08月12日 21:45:13
 * @Description:
 */
public class RedisDistributedLock implements Lock {

    private StringRedisTemplate stringRedisTemplate;
    private String lockName;
    private String uuidValue;
    private long expireTime;


    public RedisDistributedLock(StringRedisTemplate stringRedisTemplate, String lockName) {
        this.stringRedisTemplate = stringRedisTemplate;
        this.lockName = lockName;
        this.uuidValue = IdUtil.simpleUUID() + ":" + Thread.currentThread().getId();
        this.expireTime = 50L;
    }

    public String getUuidValue(){
        return this.uuidValue;
    }

    @Override
    public void lock() {
        tryLock();
    }


    @Override
    public boolean tryLock() {
        try {
            return tryLock(-1L, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 功能描述：
     *
     * @author: Xiang
     * @date: 2024年08月12日 22:17:50
     * @Description:
     * @param time 时间长度
     * @param unit 时间单元
     * @return boolean 加锁是否成功
     */
    @Override
    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
        if (time == -1L) {
            String script =
                    "if redis.call('exists',KEYS[1]) == 0  or redis.call('hexists',KEYS[1],ARGV[1]) then " +
                            "redis.call('hincrby', KEYS[1], ARGV[1] , 1) " +
                            "redis.call('expire', KEYS[1], ARGV[2]) " +
                            "return 1 " +
                            "else " +
                            "return 0 " +
                            "end";
            while (Boolean.FALSE.equals(stringRedisTemplate.execute(new DefaultRedisScript<>(script, Boolean.class), Collections.singletonList(lockName), uuidValue, String.valueOf(expireTime)))){
                TimeUnit.MILLISECONDS.sleep(60);
            }
            return true;
        }
        return false;
    }

    @Override
    public void unlock() {
        String script =
                "if redis.call('hexists',KEYS[1], ARGV[1]) == 0 then " +
                        "return nil " +
                        "elseif redis.call('hincrby',KEYS[1], ARGV[1] , -1) == 0 then " +
                        "return redis.call('del', KEYS[1]) " +
                        "else " +
                        "return 0 " +
                        "end ";
        // nil = false  1 = true 0 = false
        Long flag = stringRedisTemplate.execute(new DefaultRedisScript<>(script, Long.class), Collections.singletonList(lockName), uuidValue, String.valueOf(expireTime));
        if (null == flag){
            throw new RuntimeException("this lock doesn't exists");
        }
    }

    // 不考虑中断

    @Override
    public void lockInterruptibly() throws InterruptedException {
    }

    @Override
    public Condition newCondition() {
        return null;
    }
}
