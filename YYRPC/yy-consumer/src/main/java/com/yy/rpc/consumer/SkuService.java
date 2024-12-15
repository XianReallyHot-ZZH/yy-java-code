package com.yy.rpc.consumer;

/**
 * 模拟一个服务接口
 * （理论上这些服务的接口定义应该抽取出来，放置到common或者api中，这边都简单处理了）
 */
public interface SkuService {
    String findByName(String name);
}
