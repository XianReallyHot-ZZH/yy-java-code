package com.yy.rpc.producer.impl;

import com.yy.rpc.producer.UserService;

/**
 * 模拟一个用户服务的默认实现
 */
public class UserServiceImpl implements UserService {
    @Override
    public String findById() {
        return "user{id=1,username=youyou}";
    }
}
