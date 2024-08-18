package com.liuyuncen.controller;

import cn.hutool.core.util.IdUtil;
import com.google.common.primitives.Ints;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@RestController
@Api(tags = "微信发红包")
@Slf4j
public class RedPackageController {

    public static final String RED_PACKAGE_KEY = "redpackge:";
    public static final String RED_PACKAGE_CUSTOME_KEY = "redpackge:consume:";

    @Autowired
    private RedisTemplate redisTemplate;

    @GetMapping("/send")
    @ApiOperation("发送红包，指定红包金额和红包个数")
    public String sendRedPackage(int totalMoney, int redpackageNumber) {
        // 拆分红包
        Integer[] splitRedPackages = splitRedPackageAlgorithm(totalMoney, redpackageNumber);
        String key = RED_PACKAGE_KEY + IdUtil.simpleUUID();
        redisTemplate.opsForList().leftPushAll(key, splitRedPackages);
        redisTemplate.expire(key, 1, TimeUnit.DAYS);
        String result = key + "\t" + Ints.asList(Arrays.stream(splitRedPackages).mapToInt(Integer::valueOf).toArray());
        log.info("红包发送成功：{}", result);
        return result;
    }

    @GetMapping("rob")
    @ApiOperation("抢红包，指定红包ID 和 用户ID")
    public String robRedPackage(String redPackageKey, String userId) {
        // 验证某个用户是否抢过红包
        Object redPackage = redisTemplate.opsForHash().get(RED_PACKAGE_CUSTOME_KEY + redPackageKey, userId);
        if (null == redPackage) {
            // 没有抢过才可以抢红包
            Object partRedPackage = redisTemplate.opsForList().leftPop(RED_PACKAGE_KEY + redPackageKey);
            if (null != partRedPackage) {
                redisTemplate.opsForHash().put(RED_PACKAGE_CUSTOME_KEY + redPackageKey, userId, partRedPackage);
                log.info(" 用户：{}，抢到了 {} 红包", userId, partRedPackage);
                // TODO MQ、MySQL 记录红包数据
                return String.valueOf(partRedPackage);
            }
            return "errorCode: -1 红包抢完了";
        }
        return "errorCode： -2 " + userId + "你抢过了，不能再抢了";
    }


    /**
     * 功能描述： 二倍均值算法，拆分红包
     *
     * @param totalMoney       总金额
     * @param redPackageNumber 红包个数
     * @return 拆分后的红包，保证每个人获得的红包金额差不多
     * @author: Xiang
     * @date: 2024年08月18日 17:25:34
     * @Description:
     */
    private Integer[] splitRedPackageAlgorithm(int totalMoney, int redPackageNumber) {
        Integer[] redPackageNumbers = new Integer[redPackageNumber];
        int useMoney = 0;

        for (int i = 0; i < redPackageNumber; i++) {
            if (i == redPackageNumber - 1) {
                // 最后一个红包不需要计算了，把剩余的金额放进去就行了
                redPackageNumbers[i] = totalMoney - useMoney;
            } else {
                // 每次拆分塞进去的金额  二倍均值算法 实现
                int avgMoney = ((totalMoney - useMoney) / (redPackageNumber - i)) * 2;
                redPackageNumbers[i] = 1 + new Random().nextInt(avgMoney - 1);
            }
            useMoney = useMoney + redPackageNumbers[i];

        }
        return redPackageNumbers;
    }
}
