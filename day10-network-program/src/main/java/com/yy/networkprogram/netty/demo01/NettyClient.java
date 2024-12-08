package com.yy.networkprogram.netty.demo01;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * netty使用案例
 * 说明：
 *  使用netty构建服务端和客户端网络程序，实现互联，信息一次通信，一体验netty网络编程的魅力
 *
 * 客户端
 */
public class NettyClient {

    public static void main(String[] args) throws Exception {
        //1. 创建一个线程组
        NioEventLoopGroup group = new NioEventLoopGroup();
        //2. 创建客户端的启动助手，完成相关配置
        Bootstrap bootstrap = new Bootstrap();

        bootstrap.group(group)  //3. 设置线程组
                .channel(NioSocketChannel.class)    // 4. 设置客户端通道的实现类
                .handler(new ChannelInitializer<SocketChannel>() {
                    //5. 创建一个通道初始化对象
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        //6.往Pipeline链中添加自定义的handler
                        socketChannel.pipeline().addLast(new NettyClientHandler());
                    }
                });
        System.out.println("......客户端 启动中......");

        //7.启动客户端去连接服务器端  connect方法是异步的   sync方法是同步阻塞的
        ChannelFuture channelFuture = bootstrap.connect("127.0.0.1", 9999).sync();
        System.out.println("......客户端 启动成功......");
        //8.关闭连接(异步非阻塞)
        channelFuture.channel().closeFuture().sync();
    }




}
