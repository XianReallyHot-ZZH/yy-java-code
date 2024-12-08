package com.yy.networkprogram.netty.demo01;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * netty使用案例
 * 说明：
 *  使用netty构建服务端和客户端网络程序，实现互联，信息一次通信，一体验netty网络编程的魅力
 *
 * 服务端
 */
public class NettyServer {

    public static void main(String[] args) throws Exception {

        //1. 创建一个线程组：接收客户端连接
        NioEventLoopGroup bossGroup = new NioEventLoopGroup();
        //2. 创建一个线程组：处理网络操作（网络读写）
        NioEventLoopGroup workGroup = new NioEventLoopGroup();
        //3. 创建服务器端启动助手来配置参数
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap.group(bossGroup, workGroup) //4. 设置两个线程组
                .channel(NioServerSocketChannel.class)  //5.使用NioServerSocketChannel作为服务器端通道的实现
                .option(ChannelOption.SO_BACKLOG, 128)  //6.设置线程队列中等待连接的个数
                .childOption(ChannelOption.SO_KEEPALIVE, true)  //7.保持活动连接状态,客户端和服务端为长连接
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    // work组客户端通道的pipeline初始化逻辑
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        socketChannel.pipeline().addLast(new NettyServerHandler());
                    }
                });
        System.out.println("......服务端 启动中 init port:9999 ......");
        //10. 绑定端口 bind方法是异步的  sync方法是同步阻塞的
        ChannelFuture channelFuture = serverBootstrap.bind(9999).sync();
        System.out.println("......服务端 启动成功 ......");

        //11. 关闭通道，关闭线程组
        //获取通道：channelFuture.channel() 获取绑定到指定端口的通道。
        //获取关闭事件的未来对象：closeFuture() 获取一个 ChannelFuture 对象，该对象表示通道关闭的未来事件。
        //同步等待关闭事件完成：sync() 方法会阻塞当前线程，直到通道关闭事件完成。
        channelFuture.channel().closeFuture().sync();
        System.out.println("......服务端 channel关闭成功 ......");
        bossGroup.shutdownGracefully();
        workGroup.shutdownGracefully();
        System.out.println("......服务端 bossGroup,workGroup优雅的关闭成功 ......");
    }

}
