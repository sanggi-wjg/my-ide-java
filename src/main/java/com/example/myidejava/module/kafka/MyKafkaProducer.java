package com.example.myidejava.module.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
@Slf4j
@RequiredArgsConstructor
public class MyKafkaProducer {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final KafkaTemplate<String, String> kafkaTemplate;

    public void send(String topic, String key, String value) {
        CompletableFuture<SendResult<String, String>> future = kafkaTemplate.send(topic, key, value);
        future.whenComplete((producerRecord, exception) -> {
            logger.info("[KAFKA-PRODUCER] Send Event, Topic: {}, Key: {}, Value: {}", topic, key, value);
            if (exception != null) {
                logger.error("[KAFKA-PRODUCER] has error ", exception);
            } else {
                logger.info("[KAFKA-PRODUCER] {}", producerRecord);
            }
        });
    }

}
