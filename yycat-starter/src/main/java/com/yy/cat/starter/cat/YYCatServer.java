package com.yy.cat.starter.cat;

import com.yy.cat.starter.servlet.YYServlet;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpServerCodec;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * YYCat容器server端的实现，基于netty实现了网络层的调用处理
 */
public class YYCatServer {

    /**
     * YYServlet实现类的根路径，用于启动阶段定位业务实现类，然后完成加载
     * 例如：com.yy.cat.webapp,在该路径下，是servlet的业务实现类
     */
    private String baseYYServletPath;

    // key为servlet实现类的简单类名，value为对应servlet实例，用于后续方便定位要调用哪个servlet实例
    private Map<String, YYServlet> nameToServletMap = new ConcurrentHashMap<>();

    // key为servlet的简单类名，value为对应servlet类的全限定性类名，用于后续方便定位要调用哪个servlet实例
    private Map<String, String> nameToClassNameMap = new HashMap<>();

    public YYCatServer(String baseYYServletPath) {
        this.baseYYServletPath = baseYYServletPath;
    }

    /**
     * 启动入口
     */
    public void start() throws Exception {
        // 加载指定包中的所有Servlet的类名
        cacheServlet(baseYYServletPath);
        // 启动server服务
        runServer();
    }

    /**
     * 基于netty，实现server服务
     */
    private void runServer() throws Exception {
        EventLoopGroup parent = new NioEventLoopGroup();
        EventLoopGroup child = new NioEventLoopGroup();

        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(parent, child)
                    // 指定存放请求的队列的长度
                    .option(ChannelOption.SO_BACKLOG, 1024)
                    // 指定是否启用心跳机制来检测长连接的存活性，即客户端的存活性
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                            pipeline.addLast(new HttpServerCodec());
                            pipeline.addLast(new YYCatHandler(nameToServletMap, nameToClassNameMap));
                        }
                    });
            int port = initPort();
            ChannelFuture future = bootstrap.bind(port).sync();
            System.out.println("HeroCat启动成功：监听端口号为:" + port);
            future.channel().closeFuture().sync();
        } finally {
            parent.shutdownGracefully();
            child.shutdownGracefully();
        }
    }

    //初始化端口
    private int initPort() throws DocumentException {
        //初始化端口
        //读取配置文件Server.xml中的端口号
        InputStream in = YYCatServer.class.getClassLoader().getResourceAsStream("server.xml");
        //获取配置文件输入流
        SAXReader saxReader = new SAXReader();
        Document doc = saxReader.read(in);
        //使用SAXReader + XPath读取端口配置
        Element portEle = (Element) doc.selectSingleNode("//port");
        return Integer.valueOf(portEle.getText());
    }

    /**
     * 缓存指定包及其子孙包中的所有Servlet实现类
     * @param baseYYServletPath
     */
    private void cacheServlet(String baseYYServletPath) {
        // 获取当前项目下指定路径下的资源对象
        URL resource = this.getClass().getClassLoader().getResource(baseYYServletPath.replaceAll("\\.", "/"));
        if (resource == null) {
            System.out.println("未找到资源");
            return;
        }

        // 将URL资源转换为File资源
        File file = new File(resource.getPath());
        // 遍历指定包及其子孙包中的所有文件，查找所有.class文件
        for (File f : file.listFiles()) {
            if (f.isDirectory()) {
                // 若当前遍历的file为目录，则递归调用当前方法
                cacheServlet(baseYYServletPath + "." + f.getName());
            } else {
                // 如果是class文件，则进行缓存
                if (f.getName().endsWith(".class")) {
                    // 获取当前类名(simple name)
                    String simpleClassName = f.getName().replace(".class", "").trim();
                    // 缓存类的简单名和全限定名
                    nameToClassNameMap.put(simpleClassName, baseYYServletPath + "." + simpleClassName);
                }
            }
        }

        // 打印缓存结果
        System.out.println("扫描路径：" + baseYYServletPath + ", 识别缓存的servlet实现类：");
        for (Map.Entry<String, String> entry : nameToClassNameMap.entrySet()) {
            System.out.println(entry.getKey() + ":" + entry.getValue());
        }

    }

}
