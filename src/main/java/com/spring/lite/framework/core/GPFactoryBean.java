package com.spring.lite.framework.core;

/**
 * @author wangtianqi20
 * @Description 单例工厂的顶层设计
 * @date 2021-06-07
 */

public interface GPFactoryBean {

    /**
     * 根据beanName从Ioc容器中获取一个bean实例
     *
     * @param beanName
     * @return
     * @throws Exception
     */
    Object getBean(String beanName) throws Exception;

    Object getBean(Class<?> beanClass) throws Exception;

}
