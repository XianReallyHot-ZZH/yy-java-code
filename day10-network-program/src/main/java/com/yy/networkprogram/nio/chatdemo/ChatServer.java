package com.yy.networkprogram.nio.chatdemo;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;

/**
 * 网络多人聊天程序服务器端
 * 实现效果：多个客户端聊天，客户端显示所有客户端发送的消息，实现群聊的效果
 */
public class ChatServer {

    private ServerSocketChannel serverSocketChannel;

    private Selector selector;

    private static final int PORT = 9999;

    public static void main(String[] args) throws Exception {
        new ChatServer().start();
    }

    public ChatServer() {
        // 初始化server端网络资源
        try {
            // 1. 开启Socket监听通道
            serverSocketChannel = ServerSocketChannel.open();
            // 2. 开启选择器
            selector = Selector.open();
            // 3. 绑定端口
            serverSocketChannel.bind(new InetSocketAddress(PORT));
            // 4. 设置为非阻塞模式
            serverSocketChannel.configureBlocking(false);
            // 5. 将选择器绑定到监听通道并监听accept事件
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
            printInfo("真人网络聊天室 启动.......");
            printInfo("真人网络聊天室 初始化端口 9999.......");
            printInfo("真人网络聊天室 初始化网络ip地址 121.199.163.228.......");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 开启网络聊天室相关逻辑和资源
    public void start() throws Exception {
        try {
            while (true) { //不停监控
                if (selector.select(2000) == 0) {
                    System.out.println("Server:没有客户端连接，我去搞点兼职");
                    continue;
                }
                // selectedKeys() 返回的是当前已经准备好进行 I/O 操作的通道集合
                Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                while (iterator.hasNext()) {
                    SelectionKey key = iterator.next();
                    if (key.isAcceptable()) { //连接请求事件
                        SocketChannel sc = serverSocketChannel.accept();
                        sc.configureBlocking(false);
                        sc.register(selector, SelectionKey.OP_READ);
                        System.out.println(sc.getRemoteAddress().toString().substring(1) + "上线了...");
                    }
                    if (key.isReadable()) { //读取数据事件
                        readMsg(key);
                    }
                    //一定要把当前key删掉，防止重复处理
                    iterator.remove();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 读取来自客户端的信息，并将信息广播出去
    private void readMsg(SelectionKey key) throws Exception {
        SocketChannel channel = (SocketChannel) key.channel();
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        int count = 0;
        try {
            count = channel.read(buffer);
        } catch (IOException e) {
            System.out.println("客户端断掉了~~~");
        }
        if (count > 0) {
            String msg = new String(buffer.array());
            //打印消息
            printInfo(msg);
            //全员广播消息
            broadCast(channel, msg);
        }
    }

    private void broadCast(SocketChannel except, String msg) throws Exception {
        System.out.println("服务器进行信息的广播，广播信息：" + msg);
        // keys() 返回的是所有已注册到 Selector 的通道集合
        for (SelectionKey key : selector.keys()) {
            Channel targetChannel = key.channel();
            if (targetChannel instanceof SocketChannel && targetChannel != except) {    // 剔除掉发送信息的客户端channel
                SocketChannel destChannel = (SocketChannel) targetChannel;
                ByteBuffer buffer = ByteBuffer.wrap(msg.getBytes());
                destChannel.write(buffer);
            }
        }
    }


    private void printInfo(String str) { //往控制台打印消息
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        System.out.println("[" + sdf.format(new Date()) + "] -> " + str);
    }

}
