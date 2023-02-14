package com.example.myidejava;

import com.example.myidejava.core.AppProperty;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.PropertySource;

@PropertySource("classpath:/app.properties")
@EnableConfigurationProperties(AppProperty.class)
@SpringBootApplication
public class MyIdeJavaApplication {

    public static void main(String[] args) {
        SpringApplication.run(MyIdeJavaApplication.class, args);
    }

}
