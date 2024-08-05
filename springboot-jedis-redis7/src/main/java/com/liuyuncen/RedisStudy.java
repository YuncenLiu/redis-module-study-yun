package com.liuyuncen;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @belongsProject: redis-module-study-yun
 * @belongsPackage: com.liuyuncen
 * @author: Xiang想
 * @createTime: 2024-08-02  15:53
 * @description: TODO
 * @version: 1.0
 */
@MapperScan("com.liuyuncen.mapper")
@SpringBootApplication
public class RedisStudy {
    public static void main(String[] args) {
        SpringApplication.run(RedisStudy.class, args);
    }
}
