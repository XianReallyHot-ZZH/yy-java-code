package com.yy.disruptor.demo.multi;

import com.lmax.disruptor.*;
import com.lmax.disruptor.dsl.ProducerType;
import com.yy.disruptor.demo.single.OrderEventHandler;

import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;

/**
 * disruptor多生产者多消费者测试案例
 */
public class TestMultiConsumerAndProducerDisruptor {

    public static void main(String[] args) throws InterruptedException {
        //1.创建RingBuffer，支持多个生产者
        RingBuffer<Order> ringBuffer = RingBuffer.create(
                ProducerType.MULTI,
                new EventFactory<Order>() {
                    @Override
                    public Order newInstance() {
                        return new Order();
                    }
                },
                1024 * 8,
                new YieldingWaitStrategy()
        );

        //2.创建ringBuffer屏障
        SequenceBarrier sequenceBarrier = ringBuffer.newBarrier();

        //3.创建多个消费者数组
        Consumer[] consumers = new Consumer[10];
        for (int i = 0; i < consumers.length; i++) {
            consumers[i] = new Consumer("C" + i);
        }

        //4.构建多消费者工作池
        WorkerPool<Order> workerPool = new WorkerPool<>(ringBuffer, sequenceBarrier, new EventExceptionHandler(), consumers);

        //5.设置多个消费者的sequence序号，用于单独统计消费者的消费进度。消费进度让RingBuffer知道
        ringBuffer.addGatingSequences(workerPool.getWorkerSequences());

        //6.启动workPool
        workerPool.start(Executors.newFixedThreadPool(1));  //在实际开发，自定义线程池。


        //要生产100生产者，每个生产者发送100个数据,投递10000
        final CountDownLatch latch = new CountDownLatch(1);
        for (int i = 0; i < 100; i++) {
            Producer producer = new Producer(ringBuffer);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        // 每次一个生产者创建后就处理等待。先创建100个生产者，创建完100个生产者后再去发送数据。
                        latch.await();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    // 开始处理信息生产投递
                    for (int j = 0; j < 100; j++) {
                        producer.sendData(UUID.randomUUID().toString());
                    }
                }
            }).start();
        }
        //把所有线程都创建完
        Thread.sleep(2000);
        //唤醒，开始运行100个线程
        System.out.println("开始并发生产消息");
        latch.countDown();
        // 休眠10s，让生产者将100次循环走完
        Thread.sleep(10000);

        // 打印每个消费者的消费进度
        int total = 0;
        for (int i = 0; i < consumers.length; i++) {
            System.out.println(consumers[i].getConsumerId() + " 消费数量：" + consumers[i].getCount());
            total += consumers[i].getCount();
        }
        System.out.println("消费总数：" + total);


    }
}
