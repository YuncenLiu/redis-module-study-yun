package com.liuyuncen.service;

import com.liuyuncen.entity.Product;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.config.CronTask;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * 功能描述：
 *
 * @author: Xiang
 * @date: 2024年08月11日 00:24:44
 * @Description:
 */
@Service
@Slf4j
public class JHSTaskService {
    public static final String JHS_KEY = "jhs";
    public static final String JHS_KEY_A = "jhs:a";
    public static final String JHS_KEY_B = "jhs:b";

    @Autowired
    public RedisTemplate redisTemplate;

     @PostConstruct
    public void initJHS(){
         log.info("启动定时任务聚划算功能模拟开始....");
         initFunction2();

    }


    public void initFunction1(){
        new Thread(() ->{
            while (true){

                List<Product> list = this.getProductsFromMysql();

                // 问题？ 即使使用 LUA脚本使其变成原子操作，也有可能会导致删除过程中，查询不到数据，导致
                redisTemplate.delete(JHS_KEY);
                redisTemplate.opsForList().leftPushAll(JHS_KEY,list);

                try {
                    TimeUnit.MINUTES.sleep(1);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                log.info("1分钟刷新写入一次....");
            }
        },"t1").start();
    }

    /**
     * 功能描述：差异失效时间
     * 1、开辟两块缓存，主A从B，先更新B，再更新A，严格按这个顺序更新
     *
     * @author: Xiang
     * @date: 2024年08月11日 16:47:45
     * @Description:
     */
    public void initFunction2(){
        new Thread(() ->{
            while (true){

                List<Product> list = this.getProductsFromMysql();

                redisTemplate.delete(JHS_KEY_B);
                redisTemplate.opsForList().leftPushAll(JHS_KEY_B,list);
                redisTemplate.expire(JHS_KEY_B,86410L,TimeUnit.DAYS);

                redisTemplate.delete(JHS_KEY_A);
                redisTemplate.opsForList().leftPushAll(JHS_KEY_A,list);
                redisTemplate.expire(JHS_KEY_A,86410L,TimeUnit.DAYS);

                try {
                    TimeUnit.MINUTES.sleep(1);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                log.info("1分钟刷新写入一次....");
            }
        },"t1").start();
    }

    /**
     * 功能描述： 模拟从MySQL读取20件特价商品，加载到页面
     *
     * @author: Xiang
     * @date: 2024年08月11日 00:27:08
     * @Description:
     * @param
     * @return java.util.List<com.liuyuncen.entity.Product>
     */
    public List<Product> getProductsFromMysql(){
        List<Product> list = new ArrayList<>();
        for (int i = 1; i <= 20; i++) {
            Random random = new Random();
            int id = random.nextInt(10000);
            Product pro = new Product((long) id, "product" + i, i, "detail");
            list.add(pro);
        }
        return list;
    }


}
