package com.spring.lite.controller;

import com.spring.lite.framework.annotation.GPAutowired;
import com.spring.lite.framework.annotation.GPController;
import com.spring.lite.framework.annotation.GPRequestMapping;
import com.spring.lite.framework.annotation.GPRequestParam;
import com.spring.lite.service.IDemoService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author wangtianqi20
 * @Description
 * @date 2021-05-25
 */
@GPController
@GPRequestMapping("/web")
public class DemoController {

    @GPAutowired
    IDemoService demoService;

    @GPRequestMapping("/query")
    public void query(HttpServletRequest request, HttpServletResponse response, @GPRequestParam("name") String name) {
        System.out.println("name:" + name);
        String res = demoService.get(name);
        try {
            response.getWriter().write(res);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @GPRequestMapping("/add")
    public void addPlus(HttpServletRequest request, HttpServletResponse response, @GPRequestParam("a") int a, @GPRequestParam("b") int b) {
        System.out.println("a:" + a + "b:" + b);
        String res = "result:" + (a + b);
        try {
            response.getWriter().write(res);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
