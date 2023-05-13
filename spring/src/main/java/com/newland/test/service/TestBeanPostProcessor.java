package com.newland.test.service;

import com.spring.stereotype.Component;
import com.spring.beans.factory.config.BeanPostProcessor;

@Component
public class TestBeanPostProcessor implements BeanPostProcessor {

    @Override
    public void postProcessBeforeInitialization(String beanName, Object bean) {

    }

    @Override
    public void postProcessAfterInitialization(String beanName, Object bean) {
        System.out.println("postProcessAfterInitialization");
    }
}
