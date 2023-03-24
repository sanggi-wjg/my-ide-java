package com.example.myidejava;

import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.common.protocol.types.Field;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.HashMap;
import java.util.Map;

//@PropertySource("classpath:/app.properties")
//@EnableConfigurationProperties(AppProperty.class)
@SpringBootApplication
public class MyIdeJavaApplication {

    public static void main(String[] args) {
        SpringApplication.run(MyIdeJavaApplication.class, args);
    }


    @Bean
    public KafkaAdmin admin() {
        Map<String, Object> configs = new HashMap<>();
        configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9091");
        return new KafkaAdmin(configs);
    }
    @Bean
    public NewTopic topic() {
        return TopicBuilder.name("test")
//                .partitions(10)
//                .replicas(3)
                .build();
    }

    @KafkaListener(id = "myId", topics = "test")
    public void listen(String in) {
        System.out.println("in = " + in);
    }

    @Bean
    public ApplicationRunner runner(KafkaTemplate<String, String> template) {
        return args -> template.send("test", "test");
    }


}
