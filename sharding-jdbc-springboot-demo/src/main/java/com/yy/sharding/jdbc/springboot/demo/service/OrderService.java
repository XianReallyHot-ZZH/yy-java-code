package com.yy.sharding.jdbc.springboot.demo.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yy.sharding.jdbc.springboot.demo.pojo.Order;

import java.util.List;

public interface OrderService extends IService<Order> {

    List<Order> findByUserId(int uid);

}
