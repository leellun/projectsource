package com.spring.beans.factory.config;

import com.spring.beans.factory.ScopeEnum;

public class BeanDefinition {

    private Class beanClass;
    private ScopeEnum scope;

    public Class getBeanClass() {
        return beanClass;
    }

    public void setBeanClass(Class beanClass) {
        this.beanClass = beanClass;
    }


    public ScopeEnum getScope() {
        return scope;
    }

    public void setScope(ScopeEnum scope) {
        this.scope = scope;
    }
}
