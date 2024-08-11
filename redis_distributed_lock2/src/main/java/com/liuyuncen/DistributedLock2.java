package com.liuyuncen;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DistributedLock2 {
    public static void main(String[] args) {
        SpringApplication.run(DistributedLock2.class, args);
        System.out.println("http://localhost:7777/swagger-ui.html#/");
    }
}
