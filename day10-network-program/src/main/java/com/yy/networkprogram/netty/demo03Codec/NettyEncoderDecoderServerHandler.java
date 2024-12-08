package com.yy.networkprogram.netty.demo03Codec;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class NettyEncoderDecoderServerHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        // 配合protobuf解码器使用，这里可以直接获取到对应的对象类型
        BookMessage.Book book = (BookMessage.Book) msg;
        System.out.println("客户端 msg：" + book.getName());
    }

}
