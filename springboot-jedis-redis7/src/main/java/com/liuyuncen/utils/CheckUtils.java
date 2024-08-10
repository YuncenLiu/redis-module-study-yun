package com.liuyuncen.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

/**
 * 检查登录用户是否存在于 名单中
 * @belongsProject: redis-module-study-yun
 * @belongsPackage: com.liuyuncen.utils
 * @author: Xiang想
 * @createTime: 2024-08-09  14:47
 * @description: TODO
 * @version: 1.0
 */
@Component
@Slf4j
public class CheckUtils {

    @Autowired
    private RedisTemplate redisTemplate;

    public boolean checkWhiteBloomFilter(String checkItem,String key){
        int hashValue = Math.abs(key.hashCode());
        long index = (long) (hashValue % Math.pow(2, 32));

        Boolean existOk = redisTemplate.opsForValue().getBit(checkItem, index);
        log.info("---> key:{},对应index下标:{},是否存在:{}",key,index,existOk);
        return existOk;
    }
}
