package com.newland.springboot;

import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;

import java.util.Map;

/**
 * spring boot launch
 * @author leell
 */
public class SpringApplication {

    public static void run(Class clazz){
        AnnotationConfigWebApplicationContext applicationContext = new AnnotationConfigWebApplicationContext();
        applicationContext.register(clazz);
        applicationContext.refresh();

        WebServer webServer = getWebServer(applicationContext);
        webServer.start();
    }

    public static WebServer getWebServer(ApplicationContext applicationContext){
        Map<String, WebServer> beansOfType = applicationContext.getBeansOfType(WebServer.class);
        if (beansOfType.isEmpty()) {
            throw new NullPointerException();
        }
        if (beansOfType.size() > 1) {
            throw new IllegalStateException();
        }
        return beansOfType.values().stream().findFirst().get();
    }




}
