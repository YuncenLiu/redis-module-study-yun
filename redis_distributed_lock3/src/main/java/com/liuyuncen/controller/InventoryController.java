package com.liuyuncen.controller;

import com.liuyuncen.service.InventoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author xiang
 */
@RestController
@Api(tags = "redis 分布式锁测试")
@RequestMapping("/inventory")
public class InventoryController {

    @Autowired
    private InventoryService inventoryService;

    @ApiOperation("扣减库存，一次卖一个")
    @GetMapping("/sale")
    public String sale(){
        return inventoryService.sale();
    }


    @ApiOperation("分布式锁 setnx 命令")
    @GetMapping("/saleSetNx")
    public String saleSetNx(){
        return inventoryService.saleSetNx();
    }
}
