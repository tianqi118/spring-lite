package com.spring.lite.framework.webmvc;

import java.lang.reflect.Method;
import java.util.regex.Pattern;

/**
 * @author wangtianqi20
 * @Description 保存url与method的映射-策略模式
 * @date 2021-06-08
 */

public class GPHandlerMapping {

    protected Object controller;//目标方法所在的controller
    protected Method method;//url对应的目标方法
    protected Pattern pattern;//url封装

    public GPHandlerMapping(Pattern pattern, Object controller, Method method) {
        this.controller = controller;
        this.pattern = pattern;
        this.method = method;
    }

    public Object getController() {
        return controller;
    }

    public void setController(Object controller) {
        this.controller = controller;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public Pattern getPattern() {
        return pattern;
    }

    public void setPattern(Pattern pattern) {
        this.pattern = pattern;
    }
}
