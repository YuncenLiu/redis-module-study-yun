package com.liuyuncen.service;

import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnels;
import io.swagger.models.auth.In;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

/**
 * @author xiang
 */
@Service
@Slf4j
public class GuavaBloomFilterService {

    public static final int _1W = 10000;

    public static final int SIZE = 100 * _1W;

    // 误判率，越小，误判越少
    private static double fpp = 0.00003;

    public static BloomFilter<Integer> bloomFilter = BloomFilter.create(Funnels.integerFunnel(), SIZE, fpp);


    public void guavaBloomFilter(){
        for (int i = 0; i <= SIZE; i++) {
            bloomFilter.put(i);
        }
        ArrayList<Integer> list = new ArrayList<>(10 * _1W);
        // 从100万之后开始叠加，判断是否存在误判情况
        // 如果存在，则一定是误判了，最后计算总的误判数量

        // 因此可以测试得出，当 fpp 误判率越小，则误差越小，无限趋势于0，但不等于0
        for (int i = SIZE+1; i <= SIZE + (10 * _1W); i++) {
            if (bloomFilter.mightContain(i)){
                log.info("被误判了：{}",i);
                list.add(i);
            }
        }

        log.info("误判总数量:{}",list.size());

    }
}
