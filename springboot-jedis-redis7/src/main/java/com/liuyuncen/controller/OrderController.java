package com.liuyuncen.controller;

import com.liuyuncen.service.OrderService;
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
@Api(tags = "订单接口")
@RequestMapping("/order")
public class OrderController {

    @Resource
    private OrderService orderService;

    @ApiOperation("新增订单")
    @PostMapping( "/add")
    public void addOrder(){
        orderService.addOrder();
    }

    @ApiOperation("按照keyId查询订单")
    @GetMapping( "/add/{keyId}")
    public String getOrder(@PathVariable Integer keyId){
        return orderService.getOrderById(keyId);
    }
}
