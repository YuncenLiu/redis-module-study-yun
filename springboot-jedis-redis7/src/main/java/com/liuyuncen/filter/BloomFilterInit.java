package com.liuyuncen.filter;

import com.liuyuncen.entity.Customer;
import com.liuyuncen.service.CustomerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * @belongsProject: redis-module-study-yun
 * @belongsPackage: com.liuyuncen.filter
 * @author: Xiang想
 * @createTime: 2024-08-09  14:19
 * @description: TODO
 * @version: 1.0
 */
@Component
@Slf4j
public class BloomFilterInit {

    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private CustomerService customerService;


    @PostConstruct
    public void init() {
        log.info("bloomFilterInit init");
        // 计算 hash 可能存在负数，取绝对值
        List<Customer> list = customerService.list();
        list.forEach(customer -> {
            String key = CustomerService.CACHE_KEY + customer.getId();
            long hashValue = Math.abs(key.hashCode());
            long index = (long)(hashValue % Math.pow(2,32));
            log.info("槽位index:{}",index);
            redisTemplate.opsForValue().setBit("whitelistCustomer",index,true);
        });
    }
}
