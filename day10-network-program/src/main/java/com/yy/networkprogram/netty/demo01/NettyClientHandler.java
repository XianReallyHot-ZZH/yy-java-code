package com.yy.networkprogram.netty.demo01;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.util.CharsetUtil;

public class NettyClientHandler extends ChannelInboundHandlerAdapter {

    //通道就绪事件
    public void channelActive(ChannelHandlerContext ctx) {
        // 直接发送暗号
        ctx.writeAndFlush(Unpooled.copiedBuffer("天王盖地虎", CharsetUtil.UTF_8));
    }

    //读取数据事件
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        ByteBuf buf = (ByteBuf) msg;
        System.out.println("服务器回复的消息：" + buf.toString(CharsetUtil.UTF_8));
    }

}
