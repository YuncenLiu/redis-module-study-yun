package com.liuyuncen.bio.demo;


import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;

public class RedisServerNIO {

    static ArrayList<SocketChannel> socketlist = new ArrayList<>();
    static ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
    public static void main(String[] args) throws IOException {
        System.out.println("------- RedisServerNIO 等待启动中....");
        ServerSocketChannel serverSocket = ServerSocketChannel.open();
        serverSocket.bind(new InetSocketAddress("127.0.0.1",16379));
        // 设置为非阻塞模型
        serverSocket.configureBlocking(false);

        /*
         * Select 其实就是把NIO中用户态要遍历的 fd 数组（我们的每一个 socket 链接，安装进 ArrayList 里面）拷贝到了内核态
         * 让内核态来遍历，因为用户判断 socket 是否有数据还是要调用内核态，所有拷贝到内核后，
         * 这样遍历判断的时候就不用一直用户态、内核态切换了
         */

        while (true){
            for (SocketChannel element : socketlist) {
                int read = element.read(byteBuffer);
                if (read>0) {
                    System.out.println("---- 读取数据："+read);
                    byteBuffer.flip();
                    byte[] bytes = new byte[read];
                    byteBuffer.get(bytes);
                    System.out.println(new String(bytes));
                    byteBuffer.clear();
                }
            }

            SocketChannel socketChannel = serverSocket.accept();
            if (socketChannel != null) {
                System.out.println("--- 连接成功:");
                // 非阻塞
                socketChannel.configureBlocking(false);
                socketlist.add(socketChannel);
                System.out.println("---- socketlist size:" + socketlist.size());
            }
        }

    }
}
