package com.yy.sharding.jdbc.demo.service;

import com.yy.sharding.jdbc.demo.common.MasterSlaveDataSource;
import com.yy.sharding.jdbc.demo.pojo.User;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * user表的数据访问层
 */
public class UserService {

    // 数据写
    public boolean addUser(User user) throws Exception {
        // 获取数据源
        DataSource dataSource = MasterSlaveDataSource.getInstance();
        // 获取连接
        Connection connection = dataSource.getConnection();
        // 创建预编译的 SQL 语句
        PreparedStatement preparedStatement = connection.prepareStatement("insert into t_user(name,age,address) values(?,?,?)");
        // 设置sql 参数
        preparedStatement.setString(1, user.getName());
        preparedStatement.setInt(2, user.getAge());
        preparedStatement.setString(3, user.getAddress());
        // 执行sql
        boolean result = preparedStatement.execute();
        return result;
    }

    // 数据读
    public List<User> getUserList() throws SQLException {
        // 获取数据源
        DataSource dataSource = MasterSlaveDataSource.getInstance();
        // 获取连接
        Connection connection = dataSource.getConnection();
        // 创建预编译的 SQL 语句
        PreparedStatement preparedStatement = connection.prepareStatement("select * from t_user");
        // 执行查询
        ResultSet resultSet = preparedStatement.executeQuery();
        List<User> userList = new ArrayList<>();
        while (resultSet.next()) {
            User user = new User();
            user.setId(resultSet.getInt("id"));
            user.setName(resultSet.getString("name"));
            user.setAge(resultSet.getInt("age"));
            user.setAddress(resultSet.getString("address"));
            userList.add(user);
        }
        return userList;
    }


}
