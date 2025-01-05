package com.yy.disruptor.demo.single;

import com.lmax.disruptor.EventHandler;

/**
 * order event 事件的处理器
 */
public class OrderEventHandler implements EventHandler<OrderEvent> {
    @Override
    public void onEvent(OrderEvent orderEvent, long l, boolean b) throws Exception {
        System.err.println("线程name：" + Thread.currentThread().getName() + ", 消费者: "+ orderEvent.getValue()); //取出订单对象的价格。
    }
}
