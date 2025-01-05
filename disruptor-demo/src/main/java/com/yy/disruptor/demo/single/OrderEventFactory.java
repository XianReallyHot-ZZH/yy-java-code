package com.yy.disruptor.demo.single;

import com.lmax.disruptor.EventFactory;

/**
 * OrderEvent用户事件的创建工厂
 */
public class OrderEventFactory implements EventFactory<OrderEvent> {
    @Override
    public OrderEvent newInstance() {
        //返回空的数据对象，不是null,OrderEvent,value属性还没有赋值
        return new OrderEvent();
    }
}
