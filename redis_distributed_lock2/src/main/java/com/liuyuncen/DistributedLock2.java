package com.liuyuncen;


import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.liuyuncen.mapper")
public class DistributedLock2 {
    public static void main(String[] args) {
        SpringApplication.run(DistributedLock2.class, args);
        System.out.println("http://localhost:7777/swagger-ui.html#/");
    }
}
