package com.liuyuncen.service;

import com.liuyuncen.entity.User;
import com.mybatisflex.core.service.IService;
import org.springframework.stereotype.Service;

/**
 * @belongsProject: redis-module-study-yun
 * @belongsPackage: com.liuyuncen.service
 * @author: Xiang想
 * @createTime: 2024-08-05  14:32
 * @description: TODO
 * @version: 1.0
 */
@Service
public interface UserService extends IService<User> {


    void addUser(User user);

    User findUserById(Integer id);

    User findUserByIdHa(Integer id);
}
