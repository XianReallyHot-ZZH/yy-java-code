package com.yy.networkprogram.nio.chatdemo;

import java.util.Scanner;

public class TestChat {

    public static void main(String[] args) throws Exception {

        // 启动客户端，完成远程连接
        ChatClient chatClient = new ChatClient();

        // 数据接受线程
        new Thread(() -> {
            // 监听来自服务端的信息
            while(true) {
                try {
                    chatClient.receiveMsg();
                    Thread.sleep(1000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();


        // 模拟发送消息
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            String msg = scanner.nextLine();
            chatClient.sendMsg(msg);
        }


    }


}
