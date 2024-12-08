package com.yy.networkprogram.bio;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * 网络编程案例之BIO的使用，体验一下阻塞式IO的网络编程特性
 *
 * 基于BIO实现的服务端，一个socket代表一次连接通信，体验一下阻塞式IO的网络编程特性
 */
public class TCPServer {

    /**
     * 模拟启动一个服务端网络程序，用于接受客户端的连接请求，读取客户端的请求数据，并返回响应数据
     * 当前线程会阻塞在阻塞式accept方法上，直到有客户端连接请求，才会往下执行，以上便是阻塞式网络IO的特性
     * @param args
     */
    public static void main(String[] args) throws Exception {
        // 1、创建一个服务端socket对象，用于监听客户端的连接请求
        System.out.println("服务端启动...");
        System.out.println("初始化服务端端口：9999");
        ServerSocket serverSocket = new ServerSocket(9999);

        while(true) {
            // 2、监听客户端的连接
            Socket socket = serverSocket.accept();  // 当前线程会一直阻塞，直到有客户端连接请求，才会往下执行
            System.out.println("有客户端连接了...");
            // 3、从socket中获取输入流, 读取客户端发送的数据
            InputStream is = socket.getInputStream();
            byte[] buffer = new byte[1024];
            is.read(buffer);
            // 从socket中获取客户端的ip信息
            String clientIp = socket.getInetAddress().getHostAddress();
            System.out.println(clientIp + "说：" + new String(buffer).trim());
            // 4、从socket对象中获取输出流，返回响应数据
            socket.getOutputStream().write("没钱".getBytes());
            // 5、关闭socket资源
            socket.close();
        }
    }

}
