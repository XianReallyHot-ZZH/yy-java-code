package com.yy.networkprogram.netty.demo02chat;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.ArrayList;
import java.util.List;

public class NettyChatServerHandler extends SimpleChannelInboundHandler<String> {

    // 当server端维护的所有的channel集合
    public static List<Channel> channels = new ArrayList<>();

    //读取数据
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String s) throws Exception {
        Channel channel = ctx.channel();
        System.out.println("[Server]:接收到"+channel.remoteAddress().toString().substring(1)+"发送的消息："+s);
        // 广播
        for (Channel ch:channels) {
            if (ch!=channel)
                ch.writeAndFlush("["+channel.remoteAddress().toString().substring(1)+"]说："+s+"\n");
        }
    }

    //通道就绪
    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        Channel channel = ctx.channel();
        channels.add(channel);
        System.out.println("[Server]:"+channel.remoteAddress().toString().substring(1)+"上线");
    }

    //通道未就绪
    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        Channel inChannel=ctx.channel();
        channels.remove(inChannel);
        System.out.println("[Server]:"+inChannel.remoteAddress().toString().substring(1)+"离线");
    }
}
