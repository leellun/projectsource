package com.newland.spring.factory;

public interface BeanPostProcessor {
    /**
     * 后置处理器前置处理
     * @param beanName
     * @param bean
     */
    public void postProcessBeforeInitialization(String beanName, Object bean);

    /**
     * 后置处理器后置处理
     * @param beanName
     * @param bean
     */
    public void postProcessAfterInitialization(String beanName, Object bean);
}
