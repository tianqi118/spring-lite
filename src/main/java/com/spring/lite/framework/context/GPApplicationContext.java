package com.spring.lite.framework.context;

import com.spring.lite.framework.annotation.GPAutowired;
import com.spring.lite.framework.annotation.GPController;
import com.spring.lite.framework.annotation.GPService;
import com.spring.lite.framework.beans.GPBeanWrapper;
import com.spring.lite.framework.beans.config.GPBeanDefinition;
import com.spring.lite.framework.beans.config.GPBeanPostProcessor;
import com.spring.lite.framework.beans.support.GPBeanDefinitionReader;
import com.spring.lite.framework.beans.support.GPDefaultListableBeanFactory;
import com.spring.lite.framework.core.GPFactoryBean;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author wangtianqi20
 * @Description Ioc/DI/MVC/AOP
 * @date 2021-06-07
 */

public class GPApplicationContext extends GPDefaultListableBeanFactory implements GPFactoryBean {

    private String[] configLocations;
    private GPBeanDefinitionReader reader;
    //单例的Ioc容器
    private Map<String, Object> factoryBeanObjectCache = new ConcurrentHashMap<String, Object>();
    //通用的Ioc容器(存储所有被代理过的对象)
    private Map<String, GPBeanWrapper> factoryBeanInstanceCache = new ConcurrentHashMap<String, GPBeanWrapper>();

    public GPApplicationContext(String... configLocations) {
        this.configLocations = configLocations;
        try {
            refresh();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void refresh() throws Exception {
        //1.定位，定位配置文件
        reader = new GPBeanDefinitionReader(this.configLocations);
        //2.加载，加载配置文件、扫描相关类，封装为GPBeanDefinition
        List<GPBeanDefinition> beanDefinitions = reader.loadBeanDefinitions();
        //3.注册，把配置信息放置容器中
        doRegisterBeanDefinition(beanDefinitions);
        //4.把不是延时加载的类提前初始化
        doAutowired();
    }


    private void doRegisterBeanDefinition(List<GPBeanDefinition> beanDefinitions) throws Exception {

        for (GPBeanDefinition beanDefinition : beanDefinitions) {

            if (super.beanDefinitionMap.containsKey(beanDefinition.getFactoryBeanName())) {
                throw new Exception("The " + beanDefinition.getFactoryBeanName() + "is exists!");
            }

            super.beanDefinitionMap.put(beanDefinition.getFactoryBeanName(), beanDefinition);
        }
    }

    private void doAutowired() {
        for (Map.Entry<String, GPBeanDefinition> beanDefinitionEntry : super.beanDefinitionMap.entrySet()) {
            String beanName = beanDefinitionEntry.getKey();
            if (!beanDefinitionEntry.getValue().isLazyInit()) {
                try {
                    getBean(beanName);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }


    @Override
    public Object getBean(String beanName) throws Exception {
        GPBeanDefinition beanDefinition = super.beanDefinitionMap.get(beanName);

        GPBeanPostProcessor beanPostProcessor = new GPBeanPostProcessor();
        Object instance = instantiatBean(beanDefinition);
        if (null == instance) {
            return null;
        }
        beanPostProcessor.postProcessBeforeInitialization(instance, beanName);
        GPBeanWrapper beanWrapper = new GPBeanWrapper(instance);
        this.factoryBeanObjectCache.put(beanName, beanWrapper);
        beanPostProcessor.postProcessAfterInitialization(instance, beanName);
        populateBean(beanName, instance);

        return this.factoryBeanInstanceCache.get(beanName).getWrappedInstance();
    }


    private void populateBean(String beanName, Object instance) {
        Class clazz = instance.getClass();
        if (!(clazz.isAnnotationPresent(GPController.class) || clazz.isAnnotationPresent(GPService.class))) {
            return;
        }
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            if (!field.isAnnotationPresent(GPAutowired.class)) {
                continue;
            }
            GPAutowired autowired = field.getAnnotation(GPAutowired.class);
            String autowiredBeanName = autowired.value().trim();
            if ("".equals(autowiredBeanName)) {
                autowiredBeanName = field.getType().getName();
            }
            field.setAccessible(true);
            try {
                field.set(instance, this.factoryBeanInstanceCache.get(autowiredBeanName).getWrappedInstance());
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

    }

    private Object instantiatBean(GPBeanDefinition beanDefinition) {
        Object instance = null;
        String className = beanDefinition.getBeanClassName();
        try {
            if (this.factoryBeanObjectCache.containsKey(className)) {
                instance = this.factoryBeanInstanceCache.get(className);
            } else {
                Class<?> clazz = Class.forName(className);
                instance = clazz.newInstance();
                this.factoryBeanObjectCache.put(beanDefinition.getFactoryBeanName(), instance);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return instance;

    }

    @Override
    public Object getBean(Class<?> beanClass) throws Exception {
        return getBean(beanClass.getName());
    }

    public String[] getBeanDefinitionNames() {
        return this.beanDefinitionMap.keySet().toArray(new String[this.beanDefinitionMap.size()]);
    }

    public int getBeanDefinitionCount() {
        return this.beanDefinitionMap.size();
    }

    public Properties getConfig() {
        return this.reader.getConfig();
    }
}
