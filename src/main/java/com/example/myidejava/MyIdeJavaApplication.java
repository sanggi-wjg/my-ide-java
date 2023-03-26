package com.example.myidejava;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

//@PropertySource("classpath:/app.properties")
//@EnableConfigurationProperties(AppProperty.class)
@SpringBootApplication
public class MyIdeJavaApplication {

    public static void main(String[] args) {
        SpringApplication.run(MyIdeJavaApplication.class, args);
    }

}
