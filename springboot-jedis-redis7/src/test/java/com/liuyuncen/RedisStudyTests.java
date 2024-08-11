package com.liuyuncen;

import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnels;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class RedisStudyTests {

    @Test
    void contextLoads(){

    }

    @Test
    public void testGuava(){
        BloomFilter<Integer> bloomFilter = BloomFilter.create(Funnels.integerFunnel(), 100);
        System.out.println(bloomFilter.mightContain(1));
        System.out.println(bloomFilter.mightContain(2));

        bloomFilter.put(1);
        bloomFilter.put(2);

        System.out.println(bloomFilter.mightContain(1));
        System.out.println(bloomFilter.mightContain(2));


    }
}
