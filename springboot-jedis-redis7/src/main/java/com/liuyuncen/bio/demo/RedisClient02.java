package com.liuyuncen.bio.demo;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Scanner;

public class RedisClient02 {
    public static void main(String[] args) throws IOException {
        Socket socket = new Socket("127.0.0.1", 16379);
        OutputStream outputStream = socket.getOutputStream();

        while (true){
            Scanner scanner = new Scanner(System.in);
            String string = scanner.next();

            if (string.equalsIgnoreCase("quit")){
                break;
            }
            socket.getOutputStream().write(string.getBytes());
            System.out.println("--- RedisClient01 input quit keyword to finish ---");
        }
        outputStream.close();
        socket.close();
    }
}
