package com.yy.networkprogram.netty.demo03Codec;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.protobuf.ProtobufEncoder;

public class NettyEncoderDecoderClient {

    public static void main(String[] args) throws Exception {
        EventLoopGroup group = new NioEventLoopGroup();
        Bootstrap b = new Bootstrap();
        b.group(group)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel sc) {
                        // netty配合protobuf，使用ProtobufEncoder的编码工具
                        sc.pipeline().addLast("encoder", new ProtobufEncoder());
                        sc.pipeline().addLast(new NettyEncoderDecoderClientHandler());
                    }
                });

        // 启动客户端
        ChannelFuture cf = b.connect("127.0.0.1", 9999).sync(); // (5)

        // 等待连接关闭
        cf.channel().closeFuture().sync();
    }
}
