package cn.youyou.jvm.classloader;

/**
 * 测试类,模拟一个待生成class字节码文件的原始对象
 * 使用 javac Test.java 生成 Test.class
 */
public class Test {
    public void say() {
        System.out.println("Hello YYClassLoader");
    }
}
