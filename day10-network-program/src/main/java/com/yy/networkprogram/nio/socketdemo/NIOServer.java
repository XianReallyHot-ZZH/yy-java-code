package com.yy.networkprogram.nio.socketdemo;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

/**
 * 网络编程案例之NIO的使用，体验一下非阻塞式IO的网络编程特性
 * NIO的网络编程核心概念涉及如下：
 * 1、channel（server端的channel、客户端的channel）
 *  1.1、ServerSocketChannel（server端的channel对象，可以在客户端建立连接时获取到客户端的channel对象）
 *  1.2、SocketChannel（客户端的channel对象，可以连接到指定的服务端，基于channel完成信息通信）
 * 2、selector（多路复用选择器，用来挂载channel及其对应的监听事件,服务器和客户端都可以用这个完成事件的注册和监听）
 *  2.1、（常见）ServerSocketChannel的连接事件
 *  2.2、（常见）SocketChannel的读取事件
 * 3、SelectionKey（selector上所监听到的具体事件对象）
 *  3.1、（常见）ServerSocketChannel的连接事件对象，监听到该事件后，在ServerSocketChannel对象上可以通过accept方法获取到客户端的channel对象
 *  3.2、（常见）SocketChannel的读取事件对象，监听到该事件后，在SelectionKey对象上可以获取到对应的SocketChannel对象，进而在SocketChannel对象上可以通过read方法读取客户端发送的数据
 * <p>
 * 基于NIO实现的服务端
 */
public class NIOServer {

    public static void main(String[] args) throws Exception {

        //1. 开启一个ServerSocketChannel通道（对象）
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        //2. 开启一个Selector选择器
        Selector selector = Selector.open();
        //3. 绑定端口号9999
        System.out.println("服务器启动了...");
        System.out.println("初始化端口 9999 ");
        serverSocketChannel.bind(new InetSocketAddress(9999));
        //4. 配置server端channel为非阻塞方式
        serverSocketChannel.configureBlocking(false);
        //5. Selector选择器注册ServerSocketChannel通道，绑定连接操作
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        //6. 循环执行：监听连接事件及读取数据操作
        while (true) {
            // 6.1 监控客户端连接：判断当前服务器是否已有建立的连接
            // selecto.select()方法返回的是客户端的通道数，如果为0，则说明没有客户端连接。
            if (selector.select(2000) == 0) {
                System.out.println("服务器等待2秒，无连接");
                continue;
            }
            //6.2 得到SelectionKey,判断通道里的事件
            Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
            //遍历所有SelectionKey
            while (iterator.hasNext()) {
                SelectionKey event = iterator.next();
                // 6.1 处理连接事件，客户端先连接上，处理连接事件，然后客户端会向服务端发信息，再处理读取客户端数据事件。
                if (event.isAcceptable()) {
                    System.out.println("有客户端连接了...");
                    SocketChannel socketChannel = serverSocketChannel.accept();
                    socketChannel.configureBlocking(false);
                    // 将该socketChannel注册到selector上，监听读事件
                    //参数01-选择器
                    //参数02-服务器要监控读事件，客户端发send数据，服务端读read数据
                    //参数03-客户端传过来的数据要放在缓冲区
                    socketChannel.register(selector, SelectionKey.OP_READ, ByteBuffer.allocate(1024));
                }
                // 6.2 处理客户端的数据读取事件
                if (event.isReadable()) {
                    //数据在通道中，先得到通道
                    SocketChannel channel = (SocketChannel) event.channel();
                    //取到一个缓冲区，nio读写数据都是基于缓冲区。
                    ByteBuffer buffer = (ByteBuffer) event.attachment();
                    //从通道中将客户端发来的数据读到缓冲区
                    channel.read(buffer);
                    System.out.println("客户端说：" + new String(buffer.array()));
                }
                // 6.3 手动从集合中移除当前key,防止重复处理
                iterator.remove();
            }
        }
    }

}
