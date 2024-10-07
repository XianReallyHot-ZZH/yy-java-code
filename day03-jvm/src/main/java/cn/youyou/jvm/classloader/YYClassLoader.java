package cn.youyou.jvm.classloader;

import java.io.*;

/**
 * 自定义classloader
 * 实现从指定的文件路径下读取类的字节码文件，进而完成类的加载
 */
public class YYClassLoader extends ClassLoader {

    private String classPath;

    public YYClassLoader(String classPath) {
        this.classPath = classPath;
    }

    /**
     * 输入类的全限定名，完成类的加载，得到类的字节码对象
     * @param name
     *         The <a href="#name">类的全限定名</a> of the class
     *
     * @return
     * @throws ClassNotFoundException
     */
    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        try {
            // 获取到指定类的字节码数组（二进制字节码数据流）
            byte[] bytes = getClassBytes(name);
            if (bytes != null) {
                // 调用defineClass方法，完成类的加载，字节码数组（二进制字节码数据流）转化成字节码Class对象
                return defineClass(name, bytes, 0, bytes.length);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return super.findClass(name);
    }

    /**
     * 从指定的文件路径下读取类的字节码文件，返回二进制字节流，供后续类加载器进行加载
     * @param className
     * @return
     */
    private byte[] getClassBytes(String className) {
        String path = classPath + File.separator + className.replace(".", File.separator) + ".class";
        try (FileInputStream in = new FileInputStream(path);
             ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            byte[] buffer = new byte[2048];
            int len = 0;
            while ((len = in.read(buffer)) != -1) {
                out.write(buffer, 0, len);
            }
            return out.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
