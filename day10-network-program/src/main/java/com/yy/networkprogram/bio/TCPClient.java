package com.yy.networkprogram.bio;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Scanner;

/**
 * 网络编程案例之BIO的使用，体验一下阻塞式IO的网络编程特性
 *
 * 基于BIO实现的客户端，一个socket代表一次连接通信
 */
public class TCPClient {
    public static void main(String[] args) throws Exception {

        /**
         * 模拟键盘输入，并把输入内容传输至服务端，等待服务端的响应，完成打印
         */
        while (true) {
            // 1、 建立连接，创建socket对象
            Socket socket = new Socket("127.0.0.1", 9999);
            // 2、从连接中获取输出流对象，完成信息的发送
            OutputStream outputStream = socket.getOutputStream();
            System.out.println("请输入：");
            String msg = new Scanner(System.in).nextLine();
            outputStream.write(msg.getBytes());
            // 3、从连接中获取输入流，会一直阻塞当前线程，直到当前的连接有返回信息
            InputStream inputStream = socket.getInputStream();
            byte[] buffer = new byte[1024];
            inputStream.read(buffer);
            System.out.println("服务端返回：" + new String(buffer).trim());
            // 4、关闭当前连接
            socket.close();
        }
    }
}
