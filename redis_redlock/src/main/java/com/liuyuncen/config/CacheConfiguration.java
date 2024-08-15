package com.liuyuncen.config;

import com.mybatisflex.core.util.StringUtil;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.redisson.config.SingleServerConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @belongsProject: redis-module-study-yun
 * @belongsPackage: com.liuyuncen.config
 * @author: Xiang想
 * @createTime: 2024-08-15  11:14
 * @description: TODO
 * @version: 1.0
 */
@Configuration
@EnableConfigurationProperties(RedisProperties.class)
public class CacheConfiguration {

    @Autowired
    RedisProperties properties;

    @Bean
    RedissonClient redissonClient1() {
        Config config = new Config();
        String node = properties.getSingle().getAddress1();
        node = node.startsWith("redis://") ? node : "redis://" + node;
        SingleServerConfig serverConfig = config.useSingleServer()
                .setAddress(node)
                .setTimeout(properties.getPool().getConnTimeout())
                .setConnectionPoolSize(properties.getPool().getSize())
                .setConnectionMinimumIdleSize(properties.getPool().getMaxIdle());
        if (StringUtil.isNotBlank(properties.getPassword())) {
            serverConfig.setPassword(properties.getPassword());
        }
        return Redisson.create(config);
    }

    @Bean
    RedissonClient redissonClient2() {
        Config config = new Config();
        String node = properties.getSingle().getAddress2();
        node = node.startsWith("redis://") ? node : "redis://" + node;
        SingleServerConfig serverConfig = config.useSingleServer()
                .setAddress(node)
                .setTimeout(properties.getPool().getConnTimeout())
                .setConnectionPoolSize(properties.getPool().getSize())
                .setConnectionMinimumIdleSize(properties.getPool().getMaxIdle());
        if (StringUtil.isNotBlank(properties.getPassword())) {
            serverConfig.setPassword(properties.getPassword());
        }
        return Redisson.create(config);
    }

    @Bean
    RedissonClient redissonClient3() {
        Config config = new Config();
        String node = properties.getSingle().getAddress3();
        node = node.startsWith("redis://") ? node : "redis://" + node;
        SingleServerConfig serverConfig = config.useSingleServer()
                .setAddress(node)
                .setTimeout(properties.getPool().getConnTimeout())
                .setConnectionPoolSize(properties.getPool().getSize())
                .setConnectionMinimumIdleSize(properties.getPool().getMaxIdle());
        if (StringUtil.isNotBlank(properties.getPassword())) {
            serverConfig.setPassword(properties.getPassword());
        }
        return Redisson.create(config);
    }
}
