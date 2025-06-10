package org.example;

import org.apache.catalina.LifecycleException;
import org.example.config.webapp.TomcatServer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = "org.example")
public class Main {

    public static void main(String[] args) throws LifecycleException {
        TomcatServer.start();
    }
}