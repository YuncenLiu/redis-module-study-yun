package com.liuyuncen.controller;

import com.liuyuncen.entity.Customer;
import com.liuyuncen.service.CustomerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @belongsProject: redis-module-study-yun
 * @belongsPackage: com.liuyuncen.controller
 * @author: Xiang想
 * @createTime: 2024-08-09  13:23
 * @description: TODO
 * @version: 1.0
 */
@RestController
@Api(tags = "用户信息，布隆过滤器应用")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @ApiOperation("新增用户信息")
    @PostMapping("/addCustomer")
    public void addCustomer(@RequestBody Customer customer){
        customerService.addCustomer(customer);
    }

    @ApiOperation("/根据Id查询用户信息")
    @GetMapping("/findCustomerById")
    public Customer findCustomerById(Long id){
        return customerService.findCustomerById(id);
    }
}
