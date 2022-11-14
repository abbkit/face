package com.abbkit.face.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;

@SpringBootConfiguration(proxyBeanMethods = false)
@EnableAutoConfiguration
//@Import({ SomeConfiguration.class, AnotherConfiguration.class })
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
