package com.yy.sharding.jdbc.demo;

import com.yy.sharding.jdbc.demo.pojo.User;
import com.yy.sharding.jdbc.demo.service.UserService;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

/**
 * 读写分离测试案例
 */
public class UserServiceTest {

    private UserService userService;

    @Before
    public void init() {
        userService = new UserService();
    }

    @Test
    public void addUser() throws Exception {
        User user = new User();
        user.setName("刘备");
        user.setAge(28);
        user.setAddress("蜀国");
        userService.addUser(user);
    }

    @Test
    public void getUserList() throws Exception {
        List<User> userList = userService.getUserList();
        if (userList != null && userList.size() > 0) {
            userList.forEach(System.out::println);
        } else {
            System.out.println("没有数据");
        }

    }

}
