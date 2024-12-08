package com.yy.networkprogram.nio.chatdemo;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * 网络多人聊天程序服务器端
 * 客户端发送工具：负责连接到指定的服务端，接受消息和发送消息
 */
public class ChatClient {

    private final String serverIp = "127.0.0.1";
    private final int serverPort = 9999;

    private SocketChannel socketChannel;
    private String userName;

    public ChatClient() throws Exception {
        //1. 得到一个网络通道
        socketChannel = SocketChannel.open();
        //2. 设置非阻塞方式
        socketChannel.configureBlocking(false);
        //3. 提供服务器端的IP地址和端口号
        InetSocketAddress address = new InetSocketAddress(serverIp, serverPort);
        //4. 连接服务器端
        if (!socketChannel.connect(address)) {
            while (!socketChannel.finishConnect()) {  //nio作为非阻塞式的优势
                System.out.println("客户端连接中...，客户端可以做一些别的事情");
            }
        }
        //5. 得到客户端IP地址和端口信息，作为聊天用户名使用
        userName = socketChannel.getLocalAddress().toString().substring(1);
        System.out.println("---------------Client(" + userName + ") is ready---------------");
    }

    // 发送信息
    public void sendMsg(String msg) throws Exception {
        if (msg.equalsIgnoreCase("bye")) {
            socketChannel.close();
            return;
        }
        msg = userName + "说：" + msg;
        ByteBuffer buffer = ByteBuffer.wrap(msg.getBytes());
        socketChannel.write(buffer);
    }

    // 读取信息
    public void receiveMsg() throws Exception {
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        // read方法根据配置的是不是阻塞，表现为相应的是否阻塞，当配置为非阻塞时，会立刻返回，当配置为阻塞时，会一直等待，直到有数据进来
        int size = socketChannel.read(buffer);
        if (size > 0) {
            String msg = new String(buffer.array());
            System.out.println(msg.trim());
        }
    }

}
