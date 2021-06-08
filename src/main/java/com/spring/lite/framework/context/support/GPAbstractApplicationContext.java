package com.spring.lite.framework.context.support;

/**
 * @author wangtianqi20
 * @Description Ioc容器实现的顶层设计
 * @date 2021-06-07
 */

public abstract class GPAbstractApplicationContext {

    //受保护，只提供给子类重写
    public void refresh() throws Exception {
    }


}
