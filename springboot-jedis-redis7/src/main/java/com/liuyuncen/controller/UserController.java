package com.liuyuncen.controller;

import com.liuyuncen.entity.User;
import com.liuyuncen.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @belongsProject: redis-module-study-yun
 * @belongsPackage: com.liuyuncen.controller
 * @author: Xiang想
 * @createTime: 2024-08-02  17:44
 * @description: TODO
 * @version: 1.0
 */
@RestController
@Slf4j
@Api(tags = "用户接口")
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserService userService;

    @ApiOperation("写入user用户")
    @PostMapping( "/add")
    public void addUser(@RequestBody User user){
        userService.addUser(user);
        log.info("*** user插入成功:{}",user);
    }

    @ApiOperation("按照keyId查询订单")
    @GetMapping( "/get/{keyId}")
    @ResponseBody
    public User getOrder(@PathVariable Integer keyId){
        return userService.findUserByIdHa(keyId);
    }
}
