package com.yy.cat.starter.servlet;

/**
 * 自定义Servlet规范之响应规范
 * 说明：响应对象的功能定义的比较简单，只定义了输出响应内容的方法，只是为了能跑通项目框架，后续该部分可以继续完善增强。这部分是后续程序猿在具体编码实现servlet时能用到的对象
 */
public interface YYResponse {

    /**
     * 将响应内容输出至网络channel中
     * @param content
     * @throws Exception
     */
    void write(String content) throws Exception;

}
