package com.newland.spring.factory;

/**
 * @author leell
 */
public interface InitializingBean {
    /**
     * 初始化bean
     */
    public void afterPropertiesSet();
}
