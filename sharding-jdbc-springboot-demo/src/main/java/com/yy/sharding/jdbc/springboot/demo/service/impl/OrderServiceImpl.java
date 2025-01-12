package com.yy.sharding.jdbc.springboot.demo.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yy.sharding.jdbc.springboot.demo.mapper.OrderMapper;
import com.yy.sharding.jdbc.springboot.demo.pojo.Order;
import com.yy.sharding.jdbc.springboot.demo.service.OrderService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements OrderService {
    @Override
    public List<Order> findByUserId(int uid) {
        return baseMapper.findByUserId(uid);
    }
}
