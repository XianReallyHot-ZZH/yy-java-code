package com.yy.sharding.jdbc.springboot.demo.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yy.sharding.jdbc.springboot.demo.pojo.User;

import java.util.List;

public interface UserService extends IService<User> {

    Integer addUser(User user);

    List<User> selectLikeName(String name);

}
