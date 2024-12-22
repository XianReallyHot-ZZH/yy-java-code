package com.yy.cat.starter.servlet;

import java.util.List;
import java.util.Map;

/**
 * 自定义Servlet规范之请求规范
 * 定义了框架支持的请求对象的能力，这部分是后续程序猿在具体编码实现servlet时能用到的对象
 */
public interface YYRequest {

    // 获取URI，包含请求参数，即?后的内容
    String getUri();
    // 获取请求路径，其不包含请求参数
    String getPath();
    // 获取请求方法（GET、POST等）
    String getMethod();
    // 获取所有请求参数,key为参数名称，value为参数值列表
    Map<String, List<String>> getParameters();
    // 获取指定名称的请求参数
    List<String> getParameters(String name);
    // 获取指定名称的请求参数的第一个值
    String getParameter(String name);


}
