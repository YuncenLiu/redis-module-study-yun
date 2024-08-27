package com.liuyuncen.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @belongsProject: redis-module-study-yun
 * @belongsPackage: com.liuyuncen.controller
 * @author: Xiang想
 * @createTime: 2024-08-27  15:11
 * @description: TODO
 * @version: 1.0
 */
@RestController
@Api(tags = "Scan 扫描")
public class ScanController {

    @Autowired
    private RedisTemplate redisTemplate;


    @ApiOperation("/scan 扫描父key下的任意个数key")
    @GetMapping("/scan")
    public List<String> scan(String pattern, Integer count){
        List<String> keys = new ArrayList<>();
        ScanOptions scanOptions = ScanOptions.scanOptions()
                .match(pattern)
                .count(count)
                .build();
        try (Cursor<byte[]> cursor = Objects.requireNonNull(redisTemplate.getConnectionFactory()).getConnection().scan(scanOptions)) {
            cursor.forEachRemaining(key -> {
                keys.add(new String(key));

            });
        }


//        ScanOptions scanOptions2 = ScanOptions.scanOptions()
//                .match("" + "*")
//                .count(100)
//                .build();
//
//        CopyOnWriteArrayList<String> result = new CopyOnWriteArrayList<>();
//        Cursor<byte[]> scan = redisTemplate.getConnectionFactory().getConnection().scan(scanOptions);
//        scan.forEachRemaining(key ->{
//            result.add(new String(key));
//        });

        return keys;
    }
}
