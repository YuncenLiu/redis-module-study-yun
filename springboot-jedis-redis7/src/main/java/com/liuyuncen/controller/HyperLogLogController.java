package com.liuyuncen.controller;

import com.liuyuncen.service.HyperLogLogService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags ="亿级UV的Redis统计方案")
@RestController
@Slf4j
public class HyperLogLogController {

    @Autowired
    private HyperLogLogService hyperLogLogService;

    @ApiOperation("获取IP去重后的UV统计访问量")
    @GetMapping(value = "/uv")
    public long uv(){
        return hyperLogLogService.uv();
    }

    @ApiOperation("添加一批IP地址")
    @GetMapping(value = "/add")
    public void addIp(){
        hyperLogLogService.addIp();
    }
}
