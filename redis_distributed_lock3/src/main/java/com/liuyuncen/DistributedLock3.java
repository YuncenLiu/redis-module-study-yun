package com.liuyuncen;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class DistributedLock3 {
    public static void main(String[] args) {
        SpringApplication.run(DistributedLock3.class, args);
        System.out.println("http://localhost:7778/swagger-ui.html#/");
    }
}
