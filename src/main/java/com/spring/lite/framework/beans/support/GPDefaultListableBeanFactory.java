package com.spring.lite.framework.beans.support;

import com.spring.lite.framework.beans.config.GPBeanDefinition;
import com.spring.lite.framework.context.support.GPAbstractApplicationContext;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author wangtianqi20
 * @Description 顶层Ioc缓存
 * @date 2021-06-07
 */

public class GPDefaultListableBeanFactory extends GPAbstractApplicationContext {

    //存储注册信息的BeanDefiniton
    protected final Map<String, GPBeanDefinition> beanDefinitionMap = new ConcurrentHashMap<String, GPBeanDefinition>();


}
