package com.yy.sharding.jdbc.demo.common;

import com.alibaba.druid.pool.DruidDataSource;
import org.apache.shardingsphere.api.config.masterslave.MasterSlaveRuleConfiguration;
import org.apache.shardingsphere.shardingjdbc.api.MasterSlaveDataSourceFactory;

import javax.sql.DataSource;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * 基于sharding-jdbc的读写分离数据源工厂
 */
public class MasterSlaveDataSource {

    private static DataSource dataSource;

    public static DataSource getInstance() {
        if (dataSource != null) {
            return dataSource;
        }
        try {
            return create();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 创建多个数据源，并且基于sharding-jdbc完成读写分离配置，并最终返回被sharding-jdbc增强后的数据源
     *
     * @return
     * @throws Exception
     */
    private static DataSource create() throws Exception {

        // 配置真实数据源
        Map<String, DataSource> dataSourceMap = new HashMap<>();

        // 主数据源
        DruidDataSource masterDateSource = new DruidDataSource();
        masterDateSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        masterDateSource.setUrl("jdbc:mysql://127.0.0.1:3306/ds0?serverTimezone=Asia/Shanghai&useUnicode=true&characterEncoding=utf-8");
        masterDateSource.setUsername("root");
        masterDateSource.setPassword("123456");
        dataSourceMap.put("master", masterDateSource);

        // slave1数据源
        DruidDataSource slaveDateSource1 = new DruidDataSource();
        slaveDateSource1.setDriverClassName("com.mysql.cj.jdbc.Driver");
        slaveDateSource1.setUrl("jdbc:mysql://127.0.0.1:3306/ds1?serverTimezone=Asia/Shanghai&useUnicode=true&characterEncoding=utf-8");
        slaveDateSource1.setUsername("root");
        slaveDateSource1.setPassword("123456");
        dataSourceMap.put("slave01", slaveDateSource1);

        // slave2数据源
        DruidDataSource slaveDateSource2 = new DruidDataSource();
        slaveDateSource2.setDriverClassName("com.mysql.cj.jdbc.Driver");
        slaveDateSource2.setUrl("jdbc:mysql://127.0.0.1:3306/ds0?serverTimezone=Asia/Shanghai&useUnicode=true&characterEncoding=utf-8");
        slaveDateSource2.setUsername("root");
        slaveDateSource2.setPassword("123456");
        dataSourceMap.put("slave02", slaveDateSource2);

        // 配置sharding-jdbc的读写分离配置
        MasterSlaveRuleConfiguration masterSlaveRuleConfig = new MasterSlaveRuleConfiguration("masterSlaveDataSource", "master", Arrays.asList("slave01", "slave02"));

        // 获取被sharding-jdbc增强后的数据源
        dataSource = MasterSlaveDataSourceFactory.createDataSource(dataSourceMap, masterSlaveRuleConfig, new Properties());
        return dataSource;
    }
}
