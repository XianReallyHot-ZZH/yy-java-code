package com.yy.sharding.jdbc.springboot.demo.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yy.sharding.jdbc.springboot.demo.mapper.UserMapper;
import com.yy.sharding.jdbc.springboot.demo.pojo.User;
import com.yy.sharding.jdbc.springboot.demo.service.UserService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    @Override
    public Integer addUser(User user) {
        return baseMapper.insert(user);
    }

    @Override
    public List<User> selectLikeName(String name) {
        name = "%" + name + "%";
        return baseMapper.selectLikeName(name);
    }
}
