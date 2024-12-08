package com.yy.networkprogram.nio.socketdemo;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * 基于NIO实现的客户端
 */
public class NIOClient {

    public static void main(String[] args) throws Exception {

        //1. 得到一个网络通道
        SocketChannel socketChannel = SocketChannel.open();
        //2. 设置非阻塞方式
        socketChannel.configureBlocking(false);
        //3. 提供服务器端的IP地址和端口号
        InetSocketAddress address = new InetSocketAddress("127.0.0.1", 9999);
        //4. 连接服务器端，如果用connect()方法连接服务器不成功，则用finishConnect()方法进行连接
        if (!socketChannel.connect(address)) {  //非阻塞！
            //因为连接需要花时间，所以用while一直去尝试连接。在连接服务端时还可以做别的事，建立连接的时候不会阻塞当前的线程，体现非阻塞的io特性。
            while (!socketChannel.finishConnect()) {
                //nio作为非阻塞式的优势，如果服务器没有响应（不启动服务端)，客户端不会阻塞，最后会报错，客户端尝试链接服务器连不上。
                System.out.println("客户端连接中...，客户端可以做一些别的事情");
            }
        }
        //5. 得到一个缓冲区并存入数据
        String msg = "你好，服务器";
        ByteBuffer writerBuffer = ByteBuffer.wrap(msg.getBytes());
        //6. 发送数据
        socketChannel.write(writerBuffer);
        //阻止客户端停止，否则服务端也会停止。
        System.in.read();
    }


}
