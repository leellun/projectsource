package com.newland.test.service;

import com.newland.spring.annotation.Component;
import com.newland.spring.factory.BeanPostProcessor;

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
