package com.github.yshameer.activemq.listener;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties
public class ActiveMQListenerApp {

    public static void main(String[] args) {
        SpringApplication.run(ActiveMQListenerApp.class, args);
    }
}
