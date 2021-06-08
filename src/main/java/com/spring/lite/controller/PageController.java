package com.spring.lite.controller;

import com.spring.lite.framework.annotation.GPAutowired;
import com.spring.lite.framework.annotation.GPController;
import com.spring.lite.framework.annotation.GPRequestMapping;
import com.spring.lite.framework.annotation.GPRequestParam;
import com.spring.lite.framework.webmvc.GPModelAndView;
import com.spring.lite.service.IQueryService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * @author wangtianqi20
 * @Description
 * @date 2021-06-08
 */
@GPController
@GPRequestMapping("/")
public class PageController {

    @GPAutowired
    IQueryService queryService;

    @GPRequestMapping("/first.html")
    public GPModelAndView query(@GPRequestParam("teacher") String teacher) {
        System.out.println("teacher:" + teacher);
        String result = queryService.query(teacher);

        Map<String, Object> model = new HashMap<String, Object>();
        model.put("teacher", teacher);
        model.put("data", result);
        model.put("token", "98848392");
        return new GPModelAndView("first.html", model);
    }

}
