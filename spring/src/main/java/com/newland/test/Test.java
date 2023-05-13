package com.newland.test;

import com.spring.context.support.ClassApplicationContext;

public class Test {

    public static void main(String[] args) {
        ClassApplicationContext applicationContext = new ClassApplicationContext(AppConfig.class);
        System.out.println(applicationContext.getBean("orderService"));
        System.out.println(applicationContext.getBean("orderService"));
        System.out.println(applicationContext.getBean("orderService"));
    }
}
