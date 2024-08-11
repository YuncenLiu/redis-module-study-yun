package com.liuyuncen.controller;

import com.liuyuncen.service.GuavaBloomFilterService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "Guava布隆过滤器")
@RestController
@Slf4j
public class GuavaBloomFilterController {

    @Autowired
    private GuavaBloomFilterService guavaBloomFilterService;

    @ApiOperation("guava布隆过滤器插入100万样本数据并额外10万测试是否存在")
    @GetMapping("/guavafilter")
    public void guavaBloomFilter(){
        guavaBloomFilterService.guavaBloomFilter();
    }
}
