package com.newland.springboot.autoconfigure;

import com.newland.springboot.autoconfigure.condition.ConditionalOnClass;
import com.newland.springboot.jetty.JettyWebServer;
import com.newland.springboot.tomcat.TomcatWebServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;

@Configuration
public class WebServerAutoConfiguration implements AutoConfiguration {
    @Autowired
    private AnnotationConfigWebApplicationContext applicationContext;

    @Bean
    @ConditionalOnClass(name = "org.apache.catalina.startup.Tomcat")
    public TomcatWebServer tomcatWebServer() {
        return new TomcatWebServer(applicationContext);
    }

    @Bean
    @ConditionalOnClass(name = "org.eclipse.jetty.server.Server")
    public JettyWebServer jettyWebServer() {
        return new JettyWebServer();

    }
}
