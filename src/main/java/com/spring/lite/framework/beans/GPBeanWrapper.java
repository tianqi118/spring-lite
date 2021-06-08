package com.spring.lite.framework.beans;

/**
 * @author wangtianqi20
 * @Description
 * @date 2021-06-07
 */

public class GPBeanWrapper {

    private Object wrappedInstance;
    private Class<?> wrappedClass;

    public GPBeanWrapper(Object wrappedInstance) {
        this.wrappedInstance = wrappedInstance;
    }


    public Object getWrappedInstance() {
        return this.wrappedInstance;
    }

    //返回代理后的Class
    //可能会是$Proxy0
    public Class<?> getWrappedClass() {
        return this.wrappedInstance.getClass();
    }
}
