package com.example.myidejava.service.docker;

import com.example.myidejava.domain.docker.Container;
import com.example.myidejava.repository.docker.ContainerRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@SpringBootTest
class ContainerValidationServiceTest {
    @Autowired
    ContainerRepository containerRepository;
    @Autowired
    ContainerValidationService containerValidationService;

    @Test
    @DisplayName("validateIsContainerRunning 성공")
    void test_validateIsContainerRunning() {
        List<Container> containers = containerRepository.findAll();
        containers.forEach(container -> containerValidationService.validateIsContainerRunning(container));
    }

}