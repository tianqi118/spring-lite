package com.spring.lite.framework.context;

/**
 * @author wangtianqi20
 * @Description 通过解耦的方式获得Ioc容器的顶层设计；
 * 通过一个监听器扫描所有类，只要实现此接口；
 * 自动调用setApplicationContext()方法，从而将Ioc容器注入目标类中
 * @date 2021-06-07
 */

public interface GPApplicationContextAware {

    void setApplicationContext(GPApplicationContext applicationContext);

}
