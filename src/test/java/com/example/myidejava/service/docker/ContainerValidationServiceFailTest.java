package com.example.myidejava.service.docker;

import com.example.myidejava.core.exception.error.DockerAppException;
import com.example.myidejava.domain.docker.Container;
import com.example.myidejava.module.docker.DockerClientShortCut;
import com.example.myidejava.repository.docker.ContainerRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@SpringBootTest
class ContainerValidationServiceFailTest {
    @Autowired
    ContainerRepository containerRepository;
    @Autowired
    ContainerValidationService containerValidationService;
    @MockBean
    DockerClientShortCut dockerClientShortCut;

    @Test
    @DisplayName("validateIsContainerRunning 실패 mock")
    void test_validateIsContainerRunning_WhenIsNotRunning() {
        // given
        // when
        Mockito.when(dockerClientShortCut.isContainerStateRunning(Mockito.anyString()))
                .thenReturn(false);
        // then
        List<Container> containers = containerRepository.findAll();
        containers.forEach(container ->
                Assertions.assertThrows(DockerAppException.class, () -> containerValidationService.validateIsContainerRunning(container))
        );
        Mockito.verify(dockerClientShortCut, Mockito.times(containers.size())).isContainerStateRunning(Mockito.anyString());
    }

}