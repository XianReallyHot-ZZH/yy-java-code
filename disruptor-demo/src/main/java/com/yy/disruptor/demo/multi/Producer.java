package com.yy.disruptor.demo.multi;

import com.lmax.disruptor.RingBuffer;

public class Producer {

    // disruptor的ringBuffer，存储数据的一个容器
    private RingBuffer<Order> ringBuffer;

    //为生产者绑定ringbuffer
    public Producer(RingBuffer<Order> ringBuffer) {
        this.ringBuffer = ringBuffer;
    }

    //发送数据
    public void sendData(String uuid) {
        //1.获取到可用sequence
        long sequenceId = ringBuffer.next();
        try {
            Order order = ringBuffer.get(sequenceId);
            order.setId(uuid);
            System.out.println("生产者threadName: " + Thread.currentThread().getName() + ", sequenceId: " + sequenceId);
        } catch (Exception e) {
            System.err.println("生产者发送数据失败, " + e.getMessage());
        } finally {
            ringBuffer.publish(sequenceId);
        }
    }

}
