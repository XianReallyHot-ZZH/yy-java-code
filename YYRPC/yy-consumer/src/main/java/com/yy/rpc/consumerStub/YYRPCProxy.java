package com.yy.rpc.consumerStub;

import com.yy.rpc.producerStub.ClassInfo;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class YYRPCProxy {

    /**
     * 生成指定的类对应的代理对象
     *
     * @param targetClass
     * @return
     */
    public static Object create(Class targetClass) {

        // 实现InvocationHandler接口, 被代理的对象的每个方法调用都会被拦截，进到invoke方法中
        InvocationHandler invocationHandler = new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                // 封装一下rpc调用的对象
                ClassInfo classInfo = new ClassInfo();
                classInfo.setClassName(targetClass.getName());
                classInfo.setMethodName(method.getName());
                classInfo.setParamTypes(method.getParameterTypes());
                classInfo.setParams(args);

                // 基于netty实现一次单程调用，理论上基于netty的客户端要管理复用起来，这里先简单实现为每一次调用都新建一个客户端，进行一次channel连接
                EventLoopGroup group = new NioEventLoopGroup();
                ResultHandler resultHandler = new ResultHandler();
                try {
                    Bootstrap bootstrap = new Bootstrap();
                    bootstrap.group(group)
                            .channel(NioSocketChannel.class)
                            .handler(new ChannelInitializer<SocketChannel>() {
                                @Override
                                protected void initChannel(SocketChannel ch) throws Exception {
                                    ChannelPipeline pipeline = ch.pipeline();
                                    //编码器
                                    pipeline.addLast("encoder", new ObjectEncoder());
                                    //解码器  构造方法第一个参数设置二进制数据的最大字节数  第二个参数设置具体使用哪个类解析器
                                    pipeline.addLast("decoder", new ObjectDecoder(Integer.MAX_VALUE, ClassResolvers.cacheDisabled(null)));
                                    // 业务处理类
                                    pipeline.addLast(resultHandler);
                                }
                            });
                    ChannelFuture future = bootstrap.connect("127.0.0.1", 9999).sync();
                    future.channel().writeAndFlush(classInfo).sync();
                    System.out.println("writeAndFlush OK");
                    future.channel().closeFuture().sync();
                    System.out.println("closeFuture OK");
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    group.shutdownGracefully();
                }

                System.out.println("invoke return");
                return resultHandler.getResponse();
            }
        };

        return Proxy.newProxyInstance(targetClass.getClassLoader(), new Class[]{targetClass}, invocationHandler);
    }
}
