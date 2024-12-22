package com.yy.cat.starter.cat;

import com.yy.cat.starter.servlet.YYRequest;
import com.yy.cat.starter.servlet.YYResponse;
import com.yy.cat.starter.servlet.YYServlet;

/**
 * 默认的Servlet实现
 */
public class DefaultYYServlet extends YYServlet {
    @Override
    public void doGet(YYRequest request, YYResponse response) throws Exception {
        // http://localhost:8080/aaa/bbb/userservlet?name=xiong
        // path：/aaa/bbb/userservlet?name=xiong
        String uri = request.getUri();
        response.write("404 - no this servlet : " + (uri.contains("?")?uri.substring(0,uri.lastIndexOf("?")):uri));
    }

    @Override
    public void doPost(YYRequest request, YYResponse response) throws Exception {
        doGet(request, response);
    }
}
