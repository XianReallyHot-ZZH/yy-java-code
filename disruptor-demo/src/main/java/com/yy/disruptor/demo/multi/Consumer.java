package com.yy.disruptor.demo.multi;

import com.lmax.disruptor.WorkHandler;

import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 消费者
 */
public class Consumer implements WorkHandler<Order> {

    private String consumerId;

    private AtomicInteger count = new AtomicInteger(0);

    private Random random = new Random();

    public Consumer(String consumerId) {
        this.consumerId = consumerId;
    }

    //当生产者发布一个sequence，ringbuffer中一个序号，里面生产者生产出来的消息，生产者最后publish发布序号
    //消费者会监听，如果监听到，就会ringbuffer去取出这个序号，取到里面消息
    @Override
    public void onEvent(Order event) throws Exception {
        //模拟消费者处理消息的耗时，设定1-4毫秒之间
        TimeUnit.MILLISECONDS.sleep(random.nextInt(4) + 1);
        System.err.println("thread name:" + Thread.currentThread().getName() + ", 当前消费者: " + this.consumerId + ", 消费信息ID:" + event.getId());
        //count计数器增加+1，表示消费了一个消息
        count.incrementAndGet();
    }

    public int getCount() {
        return count.get();
    }

    public String getConsumerId() {
        return consumerId;
    }
}
