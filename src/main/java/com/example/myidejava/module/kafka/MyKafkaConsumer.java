package com.example.myidejava.module.kafka;

import com.example.myidejava.domain.docker.CodeSnippet;
import com.example.myidejava.dto.docker.CodeRequest;
import com.example.myidejava.module.docker.executor.CodeExecutorFactory;
import com.example.myidejava.module.docker.executor.ContainerCodeExecutor;
import com.example.myidejava.repository.docker.CodeSnippetRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;

@Service
@Slf4j
@RequiredArgsConstructor
public class MyKafkaConsumer {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final CodeExecutorFactory codeExecutorFactory;
    private final CodeSnippetRepository codeSnippetRepository;

    @KafkaListener(id = "my_ide", topics = "CODE_SNIPPET", groupId = "spring-boot-my-ide", autoStartup = "true")
    public void listen(
            String value,
            @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
            @Header(KafkaHeaders.RECEIVED_KEY) String key
    ) {
        logger.info("[KAFKA-CONSUMER] Consume Event, Topic: {}, Key: {}, Value: {}", topic, key, value);

        // todo refactoring
        CodeSnippet codeSnippet = codeSnippetRepository.findById(Long.parseLong(topic)).orElseThrow(EntityNotFoundException::new);
        ContainerCodeExecutor codeExecutor = codeExecutorFactory.create(codeSnippet.getContainer());
        codeExecutor.execute(codeSnippet.getContainer(), CodeRequest.builder().code(codeSnippet.getRequest()).build());
    }

}
