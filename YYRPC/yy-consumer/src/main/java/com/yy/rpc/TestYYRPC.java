package com.yy.rpc;

import com.yy.rpc.consumer.SkuService;
import com.yy.rpc.consumer.UserService;
import com.yy.rpc.consumerStub.YYRPCProxy;

public class TestYYRPC {

    public static void main(String[] args) {

        // 生成服务的代理
        SkuService skuService = (SkuService) YYRPCProxy.create(SkuService.class);
        // 服务调用（代理后远程调用）
        String result = skuService.findByName("youyou");
        System.out.println(result);

        // 生成服务的代理
        UserService userService = (UserService) YYRPCProxy.create(UserService.class);
        // 服务调用（代理后远程调用）
        String result2 = userService.findById();
        System.out.println(result2);

    }

}
