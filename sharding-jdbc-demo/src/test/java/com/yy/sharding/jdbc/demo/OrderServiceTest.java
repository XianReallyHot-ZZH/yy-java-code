package com.yy.sharding.jdbc.demo;

import com.yy.sharding.jdbc.demo.pojo.Order;
import com.yy.sharding.jdbc.demo.service.OrderService;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

/**
 * 分库分表测试案例
 */
public class OrderServiceTest {

    private OrderService orderService;

    @Before
    public void init() throws Exception {
        orderService = new OrderService();
    }

    @Test
    public void addOrder() throws Exception {
        int userId = 10;
        for (int i = 1;i <= 20; i++) {
            if (i >= 10) {
                userId = 21;
            }
            Order order = new Order();
            order.setOrderId(i);
            order.setUserId(userId);
            order.setInfo("订单信息：user_id=" + userId + ",order_id=" + i);
            boolean result = orderService.addOrderInfo(order);
            if (result) {
                System.out.println("订单" + i + "添加成功");
            }
        }
    }

    @Test
    public void findAll() throws Exception {
        List<Order> orderList = orderService.findAll();
        orderList.forEach(System.out::println);
    }

    @Test
    public void findByPage() throws Exception {
        List<Order> orderList = orderService.findByPage();
        orderList.forEach(System.out::println);
    }

    @Test
    public void findById() throws Exception {
        Order order = orderService.findById(2);
        System.out.println(order);
    }

    @Test
    public void findByUserId() throws Exception {
        List<Order> orderList = orderService.findByUserId(21);
        orderList.forEach(System.out::println);
    }

    @Test
    public void deleteAll() throws Exception {
        orderService.deleteAll();
    }


}
