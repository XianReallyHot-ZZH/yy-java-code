package com.yy.rpc.producer.impl;

import com.yy.rpc.producer.SkuService;

/**
 * 模拟一个服务的默认实现
 */
public class SkuServiceImpl implements SkuService {
    @Override
    public String findByName(String name) {
        return "sku{}:" + name;
    }
}
