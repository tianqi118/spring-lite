package com.spring.lite.framework.beans.config;

/**
 * @author wangtianqi20
 * @Description
 * @date 2021-06-07
 */

public class GPBeanPostProcessor {


    /**
     * 为bean初始化前提供回调入口
     *
     * @param bean
     * @param beanName
     * @return
     * @throws Exception
     */
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws Exception {
        return bean;
    }

    /**
     * 为bean初始化后提供回调入口
     *
     * @param bean
     * @param beanName
     * @return
     * @throws Exception
     */
    public Object postProcessAfterInitialization(Object bean, String beanName) throws Exception {
        return bean;
    }


}
