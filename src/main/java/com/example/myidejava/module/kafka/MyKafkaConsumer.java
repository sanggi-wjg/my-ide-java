package com.example.myidejava.module.kafka;

import com.example.myidejava.service.docker.ContainerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class MyKafkaConsumer {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    //    @Value("${spring.kafka.consumer.group-id}")
    //    private String groupId;
    private final ContainerService containerService;

    @KafkaListener(id = KafkaConfig.TOPIC_CODE_SNIPPET_ID, topics = KafkaConfig.TOPIC_CODE_SNIPPET, groupId = "spring-boot-my-ide", autoStartup = "true")
    public void listen(
            String value,
            @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
            @Header(KafkaHeaders.RECEIVED_KEY) String key
    ) {
        logger.info("[KAFKA-CONSUMER] Consume Event, Topic: {}, Key: {}, Value: {}", topic, key, value);
        containerService.executeCodeByCodeSnippetId(Long.parseLong(key));
    }

}
