package com.yy.networkprogram.netty.demo02chat;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class NettyChatClientHandler extends SimpleChannelInboundHandler<String> {

    // 读取数据
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String s) throws Exception {
        // 打印出来
        System.out.println(s.trim());
    }
}
