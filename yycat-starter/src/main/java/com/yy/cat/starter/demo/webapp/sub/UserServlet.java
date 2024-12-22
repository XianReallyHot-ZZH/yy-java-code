package com.yy.cat.starter.demo.webapp.sub;

import com.yy.cat.starter.servlet.YYRequest;
import com.yy.cat.starter.servlet.YYResponse;
import com.yy.cat.starter.servlet.YYServlet;

public class UserServlet extends YYServlet {
    @Override
    public void doGet(YYRequest request, YYResponse response) throws Exception {
        String uri = request.getUri();
        String path = request.getPath();
        String method = request.getMethod();
        String name = request.getParameter("name");

        String content = "uri = " + uri + "\n" +
                "path = " + path + "\n" +
                "method = " + method + "\n" +
                "param = " + name;
        response.write(content);
    }

    @Override
    public void doPost(YYRequest request, YYResponse response) throws Exception {
        doGet(request, response);
    }
}
