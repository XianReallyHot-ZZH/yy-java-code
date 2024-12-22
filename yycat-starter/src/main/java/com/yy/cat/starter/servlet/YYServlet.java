package com.yy.cat.starter.servlet;

/**
 * 自定义Servlet的规范
 * 用法：
 *  在业务应用内继承该抽象servlet类，实现具体的doGet和doPost方法，那么在项目启动的时候，内嵌的该cat容器，会自动加载该servlet，并注册到容器内，
 *  容器会根据请求路径（某种自定义的格式，包含了要调用哪种服务），匹配到对应的servlet，并根据请求类型，调用对应的doGet或doPost方法
 */
public abstract class YYServlet {

    public abstract void doGet(YYRequest request, YYResponse response) throws Exception;

    public abstract void doPost(YYRequest request, YYResponse response) throws Exception;

}
