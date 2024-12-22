package com.yy.cat.starter.demo;

import com.yy.cat.starter.cat.YYCatServer;

/**
 * 模拟一个用户的应用启动类
 * 使用内嵌的yycat web容器，自定义实现的servlet业务类在webapp路径下面
 * 项目启动后，完成自定义的servlet业务类的识别、加载，用户调用（符合请求规范的http请求）http请，匹配调用对应的servlet业务类
 *
 * 理论上这个应该是用户的一个项目，我这边简单处理了，应该是抽取出来的，然后在项目中引用yycat-starter包，然后使用它提供的功能，
 */
public class UserApplication {

    public static void main(String[] args) throws Exception {
        YYCatServer yyCatServer = new YYCatServer("com.yy.cat.starter.demo.webapp");
        yyCatServer.start();
    }

}
