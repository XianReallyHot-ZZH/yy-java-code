package cn.youyou.jvm.classloader;

import java.lang.reflect.Method;

/**
 * 测试自定义类加载器
 */
public class TestMyClassLoader {
    public static void main(String[] args) throws Exception {
        // 自定义类加载器及其加载路径
        YYClassLoader yyClassLoader = new YYClassLoader("D:\\testlib");
        // 加载指定类(全限定名)
        Class<?> clazz = yyClassLoader.loadClass("cn.youyou.jvm.classloader.Test");

        if (clazz != null) {
            Object o = clazz.newInstance();
            Method method = clazz.getMethod("say", null);
            method.invoke(o, null);
            System.out.println(clazz.getClassLoader().toString());
        }
    }
}
