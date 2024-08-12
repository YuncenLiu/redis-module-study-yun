package com.liuyuncen.service;

import cn.hutool.core.util.IdUtil;
import com.liuyuncen.entity.Inventory;
import com.liuyuncen.mapper.InventoryMapper;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Service
@Slf4j
public class InventoryService extends ServiceImpl<InventoryMapper, Inventory> {

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
            int inventoryNumber = result == null ? 0 : Integer.parseInt(result);
            log.info("sale --> 当前库存剩余：{}",inventoryNumber);
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

    /**
     * 功能描述： setNx 实现分布式锁
     * 禁止使用递归，容易造成堆栈异常
     *
     * 存在问题：A线程加锁，锁定时3秒，实际业务超出3秒，3秒后，锁自然释放，B线程进来正常操作，
     * 此时A结束后，把锁干掉了，B线程正常完成后，发现锁没了...
     *
     * @author: Xiang
     * @date: 2024年08月11日 23:41:47
     * @return java.lang.String
     */
    public String saleSetNx(){
        String retMessage = "服务" + serverName + ":" + serverPort +  "商品卖完了";
        String key = "redisLock";
        String uuidValue = IdUtil.simpleUUID()+":"+Thread.currentThread().getId();


        while (Boolean.FALSE.equals(stringRedisTemplate.opsForValue().setIfAbsent(key, uuidValue, 3, TimeUnit.SECONDS))){
            try {
                log.info("获取锁失败，重试 value为:{}",uuidValue);
                TimeUnit.MILLISECONDS.sleep(20);

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        try {
            // 查询库存信息
            String result = stringRedisTemplate.opsForValue().get("inventory002");
            int inventoryNumber = result == null ? 0 : Integer.parseInt(result);

            if (inventoryNumber > 0) {
                stringRedisTemplate.opsForValue().set("inventory002", String.valueOf(--inventoryNumber));
                Inventory inventory = new Inventory();
                inventory.setSale(inventoryNumber);
                inventory.setUuid(uuidValue);
                inventory.setType("setNx"+serverPort);
                inventory.setSucc("true");

                save(inventory);
                retMessage = "服务" + serverName + ":" + serverPort + " UUID "+uuidValue+" 成功卖出1个商品，库存剩余" + inventoryNumber;
                log.info(retMessage);
            }
        }finally {
            // 业务逻辑处理完成后， 删除
            // 存在实际业务超出预期的超出时间，容易造成 Redis 误删他人添加进来的锁
            if(uuidValue.equalsIgnoreCase(stringRedisTemplate.opsForValue().get(key))){

                // 即使如此 非原子操作也不是很好。
                stringRedisTemplate.delete(key);
            }
        }
        return retMessage;
    }


    /**
     * 功能描述：lua实现 setNx 无法解决的删除原子性问题
     *
     * @author: Xiang
     * @date: 2024年08月11日 23:41:47
     * @return java.lang.String
     */
    public String saleLua(){
        String retMessage = "服务" + serverName + ":" + serverPort +  "商品卖完了";
        String key = "redisLock";
        String uuidValue = IdUtil.simpleUUID()+":"+Thread.currentThread().getId();


        while (Boolean.FALSE.equals(stringRedisTemplate.opsForValue().setIfAbsent(key, uuidValue, 3, TimeUnit.SECONDS))){
            try {
                log.info("获取锁失败，重试 value为:{}",uuidValue);
                TimeUnit.MILLISECONDS.sleep(20);

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        try {
            // 查询库存信息
            String result = stringRedisTemplate.opsForValue().get("inventory002");
            int inventoryNumber = result == null ? 0 : Integer.parseInt(result);

            if (inventoryNumber > 0) {
                stringRedisTemplate.opsForValue().set("inventory002", String.valueOf(--inventoryNumber));
                Inventory inventory = new Inventory();
                inventory.setSale(inventoryNumber);
                inventory.setUuid(uuidValue);
                inventory.setType("setNx"+serverPort);
                inventory.setSucc("true");

                save(inventory);
                retMessage = "服务" + serverName + ":" + serverPort + " UUID "+uuidValue+" 成功卖出1个商品，库存剩余" + inventoryNumber;
                log.info(retMessage);
            }
        }finally {

            String luaScript =
                    "if redis.call('get',KEYS[1]) == ARGV[1] then " +
                        "return redis.call('del',KEYS[1]) " +
                    "else " +
                        "return 0 " +
                    "end";
            stringRedisTemplate.execute(new DefaultRedisScript<>(luaScript,Long.class), Arrays.asList(key),uuidValue);
        }
        return retMessage;
    }
}
