package com.yy.networkprogram.nio.filedemo;

import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * 操作文件的NIO使用案例
 */
public class FileNioDemo {

    /**
     * 往本地文件中写数据
     * @throws Exception
     */
    @Test
    public void  test1() throws  Exception{
        //1. 创建输出流
        FileOutputStream fos=new FileOutputStream("basic.txt");
        //2. 从流中得到一个通道
        FileChannel fc=fos.getChannel();
        //3. 提供一个缓冲区
        ByteBuffer buffer=ByteBuffer.allocate(1024);
        //4. 往缓冲区中存入数据
        String str="HelloJava";
        buffer.put(str.getBytes());
        //pos初始大小9   lim最大值
        //5. 翻转缓冲区：为什么呢？ 将当前的buffer的pos和lim翻转为对应到当前数据的起始位置和数据的长度
        buffer.flip();
        //pos初始大小0   lim9
        //6. 把缓冲区写到通道中，从pos --> lim的数据写入到channel
        fc.write(buffer);
        //7. 关闭
        fos.close();
    }

    //从本地文件中读取数据
    @Test
    public void test2() throws Exception {
        //0. 创建文件对象
        File file = new File("basic.txt");
        //1. 创建输入流
        FileInputStream fis = new FileInputStream(file);
        //2. 得到一个通道
        FileChannel fc = fis.getChannel();
        //3. 准备一个缓冲区
        ByteBuffer buffer = ByteBuffer.allocate((int) file.length());
        //4. 从通道里读取数据并存到缓冲区中
        fc.read(buffer);
        System.out.println(new String(buffer.array()));
        //5. 关闭
        fc.close();
    }

    //BIO复制文件，输入和输出，单通道
    @Test
    public void test3() throws Exception {
        FileInputStream fis = new FileInputStream("basic.txt");
        FileOutputStream fos = new FileOutputStream("basic2.txt");
        byte[] b = new byte[1024];
        while (true) {
            // 直接从输入流中读取数据
            int res = fis.read(b);
            if (res == -1) {
                break;
            }
            fos.write(b, 0, res);
        }
        fis.close();
        fos.close();
    }

    //使用NIO实现文件复制
    @Test
    public void test4() throws Exception {
        //1. 创建两个流
        FileInputStream fis = new FileInputStream("basic2.txt");
        FileOutputStream fos = new FileOutputStream("basic3.txt");
        //2. 得到两个通道
        FileChannel sourceFC = fis.getChannel();
        FileChannel destFC = fos.getChannel();
        //3. 基于通道的复制
        destFC.transferFrom(sourceFC, 0, sourceFC.size());
        //4. 关闭
        fis.close();
        fos.close();
    }
}
