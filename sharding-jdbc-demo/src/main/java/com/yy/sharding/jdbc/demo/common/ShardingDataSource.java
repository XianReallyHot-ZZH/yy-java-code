package com.yy.sharding.jdbc.demo.common;

import com.alibaba.druid.pool.DruidDataSource;
import org.apache.shardingsphere.api.config.sharding.ShardingRuleConfiguration;
import org.apache.shardingsphere.api.config.sharding.TableRuleConfiguration;
import org.apache.shardingsphere.api.config.sharding.strategy.InlineShardingStrategyConfiguration;
import org.apache.shardingsphere.shardingjdbc.api.ShardingDataSourceFactory;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * 基于sharding-jdbc的分库分表数据源工厂
 */
public class ShardingDataSource {

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

    private static DataSource create() throws Exception {

        // 配置真实数据源
        Map<String, DataSource> dataSourceMap = new HashMap<>();

        // 数据源1
        DruidDataSource dataSource01 = new DruidDataSource();
        dataSource01.setDriverClassName("com.mysql.cj.jdbc.Driver");
        dataSource01.setUrl("jdbc:mysql://127.0.0.1:3306/ds0?serverTimezone=Asia/Shanghai&useUnicode=true&characterEncoding=utf-8");
        dataSource01.setUsername("root");
        dataSource01.setPassword("123456");
        dataSourceMap.put("ds0", dataSource01);

        // 数据源2
        DruidDataSource dataSource02 = new DruidDataSource();
        dataSource02.setDriverClassName("com.mysql.cj.jdbc.Driver");
        dataSource02.setUrl("jdbc:mysql://127.0.0.1:3306/ds1?serverTimezone=Asia/Shanghai&useUnicode=true&characterEncoding=utf-8");
        dataSource02.setUsername("root");
        dataSource02.setPassword("123456");
        dataSourceMap.put("ds1", dataSource02);

        // 业务表Order表分库分表规则配置
        TableRuleConfiguration orderTableRuleConfig = new TableRuleConfiguration("t_order", "ds${0..1}.t_order${0..1}");
        // 分库策略配置，user_id % 2等于0，则进入ds0库，如果等于1则进入ds1库
        orderTableRuleConfig.setDatabaseShardingStrategyConfig(new InlineShardingStrategyConfiguration("user_id", "ds${user_id % 2}"));
        // 分表策略配置，order_id % 2等于0，则进入t_order0表，如果等于1则进入t_order1表
        orderTableRuleConfig.setTableShardingStrategyConfig(new InlineShardingStrategyConfiguration("order_id", "t_order${order_id % 2}"));

        // Sharding Rule Configuration
        ShardingRuleConfiguration shardingRuleConfiguration = new ShardingRuleConfiguration();
        shardingRuleConfiguration.getTableRuleConfigs().add(orderTableRuleConfig);

        // 创建被sharding-jdbc增强后的数据源
        dataSource = ShardingDataSourceFactory.createDataSource(dataSourceMap, shardingRuleConfiguration, new Properties());
        return dataSource;
    }
}
