package com.liuyuncen.controller;

import cn.hutool.core.collection.CollectionUtil;
import com.liuyuncen.entity.Product;
import com.liuyuncen.service.JHSTaskService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.List;

/**
 * 功能描述：
 *
 * @author: Xiang
 * @date: 2024年08月11日 00:35:04
 * @Description:
 */
@Api(tags = "聚划算")
@RestController
@RequestMapping("/product")
@Slf4j
public class JHSProductController {


    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 功能描述：
     *
     * @param page 分页
     * @param size 大小
     * @return java.util.List<com.liuyuncen.entity.Product>
     * @author: Xiang
     * @date: 2024年08月11日 00:34:48
     * @Description:
     */
    @GetMapping("/find")
    @ApiOperation("每次1页，每页显示5条数据")
    public List<Product> find(int page, int size) {
        List<Product> list = null;
        long start = (page - 1) * size;
        long end = start + size - 1;

        try {
            list = redisTemplate.opsForList().range(JHSTaskService.JHS_KEY_A, start, end);
            if (CollectionUtils.isEmpty(list)){
                log.info(" A缓存失效，B缓存顶住 ");
                list = redisTemplate.opsForList().range(JHSTaskService.JHS_KEY_B, start, end);
                if (CollectionUtils.isEmpty(list)){
                    // TODO: 查找MySQL
                }
            }
            log.info("参加活动的商家:{}",list);
        }catch (Exception e){
            // Redis 宕机、Redis网络抖动导致 timeout
            log.error("jhs execpton:{}",e);
            e.printStackTrace();
        }
        return list;
    }
}
