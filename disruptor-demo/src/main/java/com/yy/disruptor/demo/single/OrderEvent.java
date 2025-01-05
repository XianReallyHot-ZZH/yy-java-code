package com.yy.disruptor.demo.single;

/**
 * disruptor 用户事件
 */
public class OrderEvent {

    private long value; // 订单价格

    public long getValue() {
        return value;
    }

    public void setValue(long value) {
        this.value = value;
    }

}
