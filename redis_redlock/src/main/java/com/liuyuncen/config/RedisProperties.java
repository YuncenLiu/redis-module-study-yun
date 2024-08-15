package com.liuyuncen.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @belongsProject: redis-module-study-yun
 * @belongsPackage: com.liuyuncen.config
 * @author: Xiang想
 * @createTime: 2024-08-15  11:10
 * @description: TODO
 * @version: 1.0
 */

@ConfigurationProperties(prefix = "spring.redis", ignoreUnknownFields = false)
@Data
public class RedisProperties {
    private int database;
    /**
     * 等待节点回复的命令时间，从发出命令成功开始计时
     */
    private int timeout = 3000;
    private String password;
    private String mode;
    private RedisPoolProperties pool;
    private RedisSingleProperties single;
}
