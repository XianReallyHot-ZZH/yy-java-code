package com.yy.rpc.producerStub;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.reflections.Reflections;

import java.util.Set;

public class YYRPCServerInvokeHandler extends ChannelInboundHandlerAdapter {

    @Override  //读取客户端发来的数据并通过反射调用实现类的方法
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ClassInfo classInfo = (ClassInfo) msg;
        // （简单处理）生成服务实现类对象
        Object clazz = Class.forName(getImplClassName(classInfo)).newInstance();
        // 反射调用实现类相应的方法
        Object result = clazz.getClass().getMethod(classInfo.getMethodName(), classInfo.getParamTypes()).invoke(clazz, classInfo.getParams());
        // 将结果返回给客户端
        ctx.writeAndFlush(result);
    }

    /**
     * 获取对应的服务的实现类的全限定名
     *
     * @param classInfo
     * @return
     */
    private String getImplClassName(ClassInfo classInfo) throws Exception {
        // 接口的路径，这里先简单处理了（理论上这些服务的具体实现应该是要进行站桩管理的）
        String interfacePath = "com.yy.rpc.producer";
        int lastDot = classInfo.getClassName().lastIndexOf(".");
        //获取服务接口的simple名称
        String interfaceName = classInfo.getClassName().substring(lastDot);
        //接口字节码对象
        Class interfaceClass = Class.forName(interfacePath + interfaceName);
        //反射得到某接口下的所有实现类
        Reflections reflections = new Reflections(interfacePath);
        Set<Class> implClassSet = reflections.getSubTypesOf(interfaceClass);
        if(implClassSet.size()==0){
            System.out.println("未找到实现类");
            return null;
        } else if (implClassSet.size() > 1) {
            System.out.println("找到多个实现类，未明确使用哪一个");
            return null;
        } else {
            //把集合转换为数组
            Class[] classes = implClassSet.toArray(new Class[0]);
            return classes[0].getName();
        }

    }
}
