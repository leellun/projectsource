package com.newland.test.service;

import com.spring.beans.factory.annotation.Autowired;
import com.spring.stereotype.Component;
import com.spring.context.annotation.Scope;
import com.spring.beans.factory.BeanNameAware;
import com.spring.beans.factory.InitializingBean;

@Component("orderService")
@Scope("prototype")
public class OrderService implements BeanNameAware, InitializingBean {

    @Autowired
    private UserService userService;

    private String beanName;

    @Override
    public void afterPropertiesSet() {
        System.out.println("初始化");
    }

    @Override
    public void setBeanName(String name) {
        System.out.println(name);
        this.beanName = name;
    }

    public UserService getUserService() {
        return userService;
    }
}
