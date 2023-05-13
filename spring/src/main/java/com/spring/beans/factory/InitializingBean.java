package com.spring.beans.factory;

/**
 * @author leell
 */
public interface InitializingBean {
    /**
     * 初始化bean
     */
    public void afterPropertiesSet();
}
