package com.liuyuncen.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Service
@Slf4j
public class InventoryService {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Value("${server.port}")
    private String serverPort;

    @Value("${spring.application.name}")
    private String serverName;

    private Lock lock = new ReentrantLock();

    /**
     * 功能描述： 单机版本 自身Lock的形式，实现单机锁
     * @url: <a href="http://localhost:7777/swagger-ui.html#/redis%20%E5%88%86%E5%B8%83%E5%BC%8F%E9%94%81%E6%B5%8B%E8%AF%95/saleUsingGET">触发请求</a>
     * @author: Xiang
     * @date: 2024年08月11日 22:33:23
     * @Description:
     * @return java.lang.String
     */
    public String sale(){
        String retMessage = "服务" + serverName + ":" + serverPort +  "商品卖完了";
        lock.lock();
        try {
            // 查询库存信息
            String result = stringRedisTemplate.opsForValue().get("inventory001");
            Integer inventoryNumber = result == null ? 0 : Integer.parseInt(result);
            log.info("当前库存剩余：{}",inventoryNumber);
            if (inventoryNumber > 0) {
                stringRedisTemplate.opsForValue().set("inventory001", String.valueOf(--inventoryNumber));
                retMessage = "服务" + serverName + ":" + serverPort + "成功卖出1个商品，库存剩余" + inventoryNumber;
                log.info(retMessage);
            }
        }finally {
            lock.unlock();
        }
        return retMessage;
    }
}
