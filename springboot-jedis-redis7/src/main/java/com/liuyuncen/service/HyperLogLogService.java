package com.liuyuncen.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Random;

@Service
@Slf4j
public class HyperLogLogService {

    @Autowired
    private RedisTemplate redisTemplate;

    @PostConstruct
    public void initIp() {
//        addIp();
    }

    public long uv(){
        return redisTemplate.opsForHyperLogLog().size("hll");
    }

    public void addIp(){
        new Thread(() -> {
            String ip = null;
            for (int i = 0; i < 200; i++) {
                Random random = new Random();
//                ip = random.nextInt(256)
//                        + "." + random.nextInt(256)
//                        + "." + random.nextInt(256)
//                        + "." + random.nextInt(256);

                ip = "192"
                        + "." + "168"
                        + "." + "133"
                        + "." + random.nextInt(256);
                Long hll = redisTemplate.opsForHyperLogLog().add("hll", ip);
                log.info("ip={},该ip地址访问首页的次数 = {}", ip, hll);

//                try {
//                    TimeUnit.SECONDS.sleep(3);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
            }
        }, "T1").start();
    }

}
