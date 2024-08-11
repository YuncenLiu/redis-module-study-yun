package com.liuyuncen;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
@MapperScan("com.liuyuncen.mapper")
public class DistributedLock3 {
    public static void main(String[] args) {
        SpringApplication.run(DistributedLock3.class, args);
        System.out.println("http://localhost:7778/swagger-ui.html#/");
    }
}
