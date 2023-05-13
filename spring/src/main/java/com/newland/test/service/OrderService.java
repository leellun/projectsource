package com.newland.test.service;

import com.newland.spring.annotation.Autowired;
import com.newland.spring.annotation.Component;
import com.newland.spring.annotation.Scope;
import com.newland.spring.factory.BeanNameAware;
import com.newland.spring.factory.InitializingBean;

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
