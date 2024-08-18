package com.liuyuncen.bio.demo;

import cn.hutool.core.util.IdUtil;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class RedisServerBIO {
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(16379);
        while (true){
            System.out.println("--- 111 等待连接");
            Socket accept = serverSocket.accept();
            System.out.println("--- 222 连接成功");

            InputStream inputStream = accept.getInputStream();
            int length = -1;

            byte[] bytes = new byte[1024];
            System.out.println("--- 333 读取连接");
            while ((length = inputStream.read(bytes)) != -1){
                System.out.println("--- 444 成功读取 " + new String(bytes,0, length));
                System.out.println("=======================" + "\t" + IdUtil.simpleUUID());
                System.out.println();
            }
            inputStream.close();
            accept.close();
        }
    }
}
