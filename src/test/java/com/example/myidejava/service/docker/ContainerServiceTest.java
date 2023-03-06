package com.example.myidejava.service.docker;

import com.example.myidejava.core.exception.error.NotFoundException;
import com.example.myidejava.domain.docker.Container;
import com.example.myidejava.dto.docker.CodeRequest;
import com.example.myidejava.dto.docker.CodeResponse;
import com.example.myidejava.dto.docker.ContainerResponse;
import com.example.myidejava.repository.docker.ContainerRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;


@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@SpringBootTest
class ContainerServiceTest {
    @Autowired
    ContainerService containerService;
    @Autowired
    ContainerRepository containerRepository;

    CodeResponse whenExecuteCode(String[] given) {
        CodeRequest codeRequest = CodeRequest.builder().code(given[2]).build();
        Container container = containerRepository.findByLanguageNameAndLanguageVersion(given[0], given[1]).orElseThrow();
        return containerService.executeCode(container.getId(), codeRequest);
    }

    @Test
    void test_initialize() {
        // when
        List<ContainerResponse> containerResponseListOnServer = containerService.getAllContainersOnServer();
        List<ContainerResponse> containerResponseList = containerService.getAllContainers();
        // then
        Assertions.assertEquals(containerResponseListOnServer.size(), containerResponseList.size(), "서버 컨테이너 개수와 디비에 저장된 컨테이너 개수는 같아야 한다.");
        containerResponseList.stream().forEach(containerResponse -> {
            Assertions.assertNotNull(containerResponse.getContainerId());
            Assertions.assertNotNull(containerResponse.getDockerImageName());
            Assertions.assertNotNull(containerResponse.getLanguageName());
            Assertions.assertNotNull(containerResponse.getLanguageVersion());
            Assertions.assertNotNull(containerResponse.getContainerStatus());
            Assertions.assertNotNull(containerResponse.getContainerState());
            Assertions.assertNotNull(containerResponse.getCodeExecutorType());
            Assertions.assertEquals("running", containerResponse.getContainerState());
        });
    }

    @Test
    void test_getContainerById() {
        // given
        Long containerId = 1L;
        // when
        Container container = containerService.getContainerById(containerId);
        // then
        Assertions.assertEquals(container.getId(), containerId);
    }

    @Test
    void test_getContainerById_raise() {
        // given
        Long containerId = 999L;
        // when then
        Assertions.assertThrows(NotFoundException.class,
                () -> containerService.getContainerById(containerId));
    }

    @Test
    void Python_3_8_컨테이너_코드_실행() {
        // given
        String[] given = {"python", "3.8", "print(12345)"};
        // when
        CodeResponse codeResponse = whenExecuteCode(given);
        // then
        Assertions.assertEquals("12345\n", codeResponse.getOutput());
        Assertions.assertTrue(codeResponse.getError().isEmpty());
    }

    @Test
    void Python_2_7_컨테이너_코드_실행() {
        // given
        String[] given = {"python", "2.7", "print(12345)"};
        // when
        CodeResponse codeResponse = whenExecuteCode(given);
        // then
        Assertions.assertEquals("12345\n", codeResponse.getOutput());
        Assertions.assertTrue(codeResponse.getError().isEmpty());
    }

    @Test
    void PHP_7_4_컨테이너_코드_실행() {
        // given
        String[] given = {"php", "7.4", "<?php\n print_r(['Hello' => 'World']);"};
        // when
        CodeResponse codeResponse = whenExecuteCode(given);
        // then
        assertThat(codeResponse.getOutput(), containsString("Hello"));
        assertThat(codeResponse.getOutput(), containsString("World"));
        Assertions.assertTrue(codeResponse.getError().isEmpty());
    }

    @Test
    void PHP_8_2_컨테이너_코드_실행() {
        // given
        String[] given = {"php", "8.2", "<?php\n print_r(['Hello' => 'World']);"};
        // when
        CodeResponse codeResponse = whenExecuteCode(given);
        // then
        assertThat(codeResponse.getOutput(), containsString("Hello"));
        assertThat(codeResponse.getOutput(), containsString("World"));
        Assertions.assertTrue(codeResponse.getError().isEmpty());
    }

    @Test
    void GCC_4_9_컨테이너_코드_실행() {
        // given
        String[] given = {"gcc", "4.9", "#include <stdio.h>\n\nint main()\n{\n    printf(\"Hello World\");\n    return 0;\n}"};
        // when
        CodeResponse codeResponse = whenExecuteCode(given);
        // then
        assertThat(codeResponse.getOutput(), containsString("Hello"));
        assertThat(codeResponse.getOutput(), containsString("World"));
        Assertions.assertTrue(codeResponse.getError().isEmpty());
    }

}