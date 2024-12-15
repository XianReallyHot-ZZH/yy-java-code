package com.yy.rpc.producerStub;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;

/**
 * 模拟一个服务端
 * 基于netty实现rpc的网络通信实现，接收到rpc-consumer的请求，利用反射调用相应的服务的实现方法，然后将调用结果返回给rpc-consumer
 */
public class YYRPCServer {

    private int port;

    public YYRPCServer(int port) {
        this.port = port;
    }

    public void start() {
        EventLoopGroup bossGroup = new NioEventLoopGroup(2);
        EventLoopGroup workerGroup = new NioEventLoopGroup(8);

        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    .localAddress(port)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                            // 编码器
                            pipeline.addLast("encoder", new ObjectEncoder());
                            // 解码器, ObjectDecoder 用于将接收到的字节数据解码为 Java 对象,使用的java默认的序列化手段
                            pipeline.addLast("decoder", new ObjectDecoder(Integer.MAX_VALUE, ClassResolvers.cacheDisabled(null)));
                            // 任务处理
                            pipeline.addLast(new YYRPCServerInvokeHandler());
                        }
                    });
            ChannelFuture future = serverBootstrap.bind(port).sync();
            System.out.println("YYRPC-服务端启动成功");
            future.channel().closeFuture().sync();
        } catch (Exception e) {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    public static void main(String[] args) {
        new YYRPCServer(9999).start();
    }

}
