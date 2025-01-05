package com.yy.disruptor.demo.multi;

import com.lmax.disruptor.ExceptionHandler;

public class EventExceptionHandler implements ExceptionHandler<Order> {

    //消费时出现异常
    @Override
    public void handleEventException(Throwable ex, long sequence, Order event) {
        System.err.println("消费时出现异常:" + ex.getMessage());
    }

    //启动时出现异常
    @Override
    public void handleOnStartException(Throwable ex) {
        System.err.println("启动时出现异常:" + ex.getMessage());
    }

    //关闭时出现异常
    @Override
    public void handleOnShutdownException(Throwable ex) {
        System.err.println("关闭时出现异常:" + ex.getMessage());
    }
}
