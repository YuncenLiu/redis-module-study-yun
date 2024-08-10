package com.liuyuncen.service;

import com.liuyuncen.entity.Customer;
import com.liuyuncen.mapper.CustomerMapper;
import com.liuyuncen.utils.CheckUtils;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * @belongsProject: redis-module-study-yun
 * @belongsPackage: com.liuyuncen.service
 * @author: Xiang想
 * @createTime: 2024-08-09  11:58
 * @description: TODO
 * @version: 1.0
 */
@Slf4j
@Service
public class CustomerService extends ServiceImpl<CustomerMapper, Customer> {

    public static final String CACHE_KEY = "customer:";

    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private CheckUtils checkUtils;

    public void addCustomer(Customer customer){
        if (save(customer)) {
            // 插入成功
            String key = CACHE_KEY + customer.getId();
            redisTemplate.opsForValue().set(key, customer,3L, TimeUnit.DAYS);

        }
    }

    public Customer findCustomerById(Long id){
        Customer customer = null;
        String key = CACHE_KEY + id;

        /* ****************** 布隆过滤器 ************************/
        if (!checkUtils.checkWhiteBloomFilter("whitelistCustomer",key)) {
            log.info(" 白名单无此用户，不可以访问, {}",id);
            return null;
        }

        /* ****************** 布隆过滤器 ************************/
        customer = (Customer) redisTemplate.opsForValue().get(key);
        if (customer == null){
            // 查询 MySQL
            customer = getById(id);
            if (customer != null){
                // 回写，保证双写一致性
                redisTemplate.opsForValue().set(key,customer,3L, TimeUnit.DAYS);
            }
        }
        return customer;
    }

}
