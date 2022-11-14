package com.abbkit.face.web;

import com.abbkit.face.engine.impl.arcsoft.EnableArcSoft;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootConfiguration(proxyBeanMethods = false)
@EnableAutoConfiguration
//@Import({ SomeConfiguration.class, AnotherConfiguration.class })
@EnableScheduling
@EnableArcSoft
@ComponentScan("com.abbkit.face")
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
