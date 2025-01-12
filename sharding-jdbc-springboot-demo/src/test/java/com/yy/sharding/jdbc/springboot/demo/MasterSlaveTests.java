package com.yy.sharding.jdbc.springboot.demo;

import com.yy.sharding.jdbc.springboot.demo.pojo.User;
import com.yy.sharding.jdbc.springboot.demo.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

/**
 * 读写分离案例
 */
@SpringBootTest
public class MasterSlaveTests {

    @Autowired
    private UserService userService;

    @Test
    void addUser() throws Exception {
        User user = new User();
        user.setName("刘皇叔");
        user.setAge(28);
        user.setAddress("蜀国");
        userService.addUser(user);
    }

    @Test
    void getUserList() throws Exception {
        List<User> userList = userService.selectLikeName("刘");
        userList.forEach(System.out::println);
    }


}
