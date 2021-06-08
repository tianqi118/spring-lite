package com.spring.lite.framework.webmvc;

import java.util.Map;

/**
 * @author wangtianqi20
 * @Description 封装页面模板与参数
 * @date 2021-06-08
 */

public class GPModelAndView {

    private String viewName;//页面模板名称
    private Map<String, ?> model;//页面传递参数

    public GPModelAndView(String viewName) {
        this(viewName, null);
    }

    public GPModelAndView(String viewName, Map<String, ?> model) {
        this.viewName = viewName;
        this.model = model;
    }


    public String getViewName() {
        return viewName;
    }

    public void setViewName(String viewName) {
        this.viewName = viewName;
    }

    public Map<String, ?> getModel() {
        return model;
    }

    public void setModel(Map<String, ?> model) {
        this.model = model;
    }
}
