package com.newland.springboot.jetty;

import com.newland.springboot.WebServer;

public class JettyWebServer implements WebServer {

    static {
        System.out.println("JettyWebServer");
    }

    @Override
    public void start() {
        System.out.println("启动Jetty");
    }
}
