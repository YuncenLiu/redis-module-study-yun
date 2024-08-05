package com.liuyuncen.service.impl;

import com.liuyuncen.entity.User;
import com.liuyuncen.mapper.UserMapper;
import com.liuyuncen.service.UserService;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;


/**
 * @belongsProject: redis-module-study-yun
 * @belongsPackage: com.liuyuncen.service.impl
 * @author: Xiang想
 * @createTime: 2024-08-05  15:14
 * @description: TODO
 * @version: 1.0
 */
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    public static final String USER_KEY = "user:";

    @Autowired
    private RedisTemplate redisTemplate;


    @Override
    public void addUser(User user) {
        if (save(user)) {
            // redis 缓存key
            String key = USER_KEY + user.getId();
            redisTemplate.opsForValue().set(key, user);
        }
    }


    /**
     * @description: 业务逻辑没有写错，对于 QPS<1000 可以使用
     * @author: Xiang想
     * @date: 2024/8/5 14:46
     * @param: [id]
     * @return: com.liuyuncen.entities.User
     **/
    @Override
    public User findUserById(Integer id){
        User user = null;
        String key  = USER_KEY + id;
        user = (User) redisTemplate.opsForValue().get(key);

        if (null == user) {
            user = getById(id);
            if (null == user) {
                // redis 和 mysql 都没有数据
                // 具体化、防止多次穿透，业务规定，记录下这个null值的key，列入黑名单或记录异常
                log.info("*** Redis查询不到这个key值：{}",key);
            }
        }
        return user;
    }

    /**
     * @description: 保证数据双写一致性回写Redis，合理避免缓存击穿的 双检加锁策略
     * 以牺牲 QPS 代价保证 MySQL数据库相对安全和数据一致性。
     * @author: Xiang想
     * @date: 2024/8/5 15:35
     * @param: [id]
     * @return: com.liuyuncen.entity.User
     **/
    @Override
    public User findUserByIdHa(Integer id){
        User user = null;
        String key  = USER_KEY + id;
        user = (User) redisTemplate.opsForValue().get(key);
        if (null == user) {
            // 如果不加锁，无法保证 Redis读取和 MySQL写入是原子操作
            synchronized (UserService.class){
                user = (User) redisTemplate.opsForValue().get(key);
                // 2次查询，都没有数据再前往 MySQL查询
                if (null == user) {
                    user = getById(id);
                    if (null == user) {
                        // redis 和 mysql 都没有数据
                        // 具体化、防止多次穿透，业务规定，记录下这个null值的key，列入黑名单或记录异常
                        log.info("*** Redis查询不到这个key值：{}",key);
                    }else{
                        redisTemplate.opsForValue().setIfAbsent(key,user,7L, TimeUnit.DAYS);
                    }
                }
            }
        }
        return user;
    }
}
