package com.liuyuncen.config;

import lombok.Data;

/**
 * @belongsProject: redis-module-study-yun
 * @belongsPackage: com.liuyuncen.config
 * @author: Xiang想
 * @createTime: 2024-08-15  11:12
 * @description: TODO
 * @version: 1.0
 */

@Data
public class RedisPoolProperties {

    private int maxIdle;
    private int minIdle;
    private int maxActive;
    private int maxWait;
    private int connTimeout = 10000;
    private int soTimeout;
    /**
     * 池大小
     */
    private int size;
}
