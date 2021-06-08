package com.spring.lite.controller;

import com.spring.lite.framework.annotation.GPAutowired;
import com.spring.lite.framework.annotation.GPController;
import com.spring.lite.framework.annotation.GPRequestMapping;
import com.spring.lite.framework.annotation.GPRequestParam;
import com.spring.lite.framework.webmvc.GPModelAndView;
import com.spring.lite.service.IModifyService;
import com.spring.lite.service.IQueryService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author wangtianqi20
 * @Description
 * @date 2021-06-08
 */
@GPController
@GPRequestMapping("/web")
public class BizController {

    @GPAutowired
    IQueryService queryService;

    @GPAutowired
    IModifyService modifyService;

    @GPRequestMapping("/query.json")
    public GPModelAndView query(HttpServletRequest request, HttpServletResponse response, @GPRequestParam("name") String name) {
        System.out.println("name:" + name);
        String result = queryService.query(name);
        return out(response, result);
    }

    @GPRequestMapping("/add.json")
    public GPModelAndView add(HttpServletRequest request, HttpServletResponse response, @GPRequestParam("name") String name, @GPRequestParam("addr") String addr) {
        System.out.println("name:" + name);
        String result = modifyService.add(name, addr);
        return out(response, result);
    }

    @GPRequestMapping("/edit.json")
    public GPModelAndView edit(HttpServletRequest request, HttpServletResponse response, @GPRequestParam("id") Integer id, @GPRequestParam("name") String name) {
        System.out.println("ID:" + id + "name:" + name);
        String result = modifyService.edit(id, name);
        return out(response, result);
    }

    private GPModelAndView out(HttpServletResponse response, String str) {
        try {
            response.getWriter().write(str);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
