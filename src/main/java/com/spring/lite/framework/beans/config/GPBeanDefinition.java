package com.spring.lite.framework.beans.config;

/**
 * @author wangtianqi20
 * @Description 存储配置文件信息-保存至内存中
 * @date 2021-06-07
 */

public class GPBeanDefinition {
    private String beanClassName;//原生bean的全类名
    private boolean lazyInit = false;
    private String factoryBeanName;//保存beanName，Ioc容器中的Key

    public String getBeanClassName() {
        return beanClassName;
    }

    public void setBeanClassName(String beanClassName) {
        this.beanClassName = beanClassName;
    }

    public boolean isLazyInit() {
        return lazyInit;
    }

    public void setLazyInit(boolean lazyInit) {
        this.lazyInit = lazyInit;
    }

    public String getFactoryBeanName() {
        return factoryBeanName;
    }

    public void setFactoryBeanName(String factoryBeanName) {
        this.factoryBeanName = factoryBeanName;
    }
}
