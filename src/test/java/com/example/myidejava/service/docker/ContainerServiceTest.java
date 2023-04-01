package com.example.myidejava.service.docker;

import com.example.myidejava.core.exception.error.NotFoundException;
import com.example.myidejava.domain.docker.Container;
import com.example.myidejava.dto.docker.CodeRequest;
import com.example.myidejava.dto.docker.CodeSnippetResponse;
import com.example.myidejava.dto.docker.ContainerResponse;
import com.example.myidejava.module.docker.executor.CodeExecutorFactory;
import com.example.myidejava.module.docker.executor.ContainerCodeExecutor;
import com.example.myidejava.repository.docker.ContainerRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
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
    CodeSnippetService codeSnippetService;
    @Autowired
    ContainerRepository containerRepository;
    @Autowired
    CodeExecutorFactory codeExecutorFactory;

    CodeSnippetResponse whenExecuteCode(String[] given) {
        CodeRequest codeRequest = CodeRequest.builder().code(given[2]).build();
        Container container = containerRepository.findByLanguageNameAndLanguageVersion(given[0], given[1]).orElseThrow();
        return containerService.executeCode(container.getId(), codeRequest, null);
    }

    CodeSnippetResponse whenRequestExecuteCode(String[] given) {
        CodeRequest codeRequest = CodeRequest.builder().code(given[2]).build();
        Container container = containerRepository.findByLanguageNameAndLanguageVersion(given[0], given[1]).orElseThrow();
        CodeSnippetResponse codeSnippetResponse = containerService.requestCodeSnippetToExecute(container.getId(), codeRequest, null);
        containerService.executeCodeByCodeSnippetId(codeSnippetResponse.getId());
        return codeSnippetResponse;
    }

    @Test
    @DisplayName("Initialize 성공")
    void test_initialize() {
        // when
        List<ContainerResponse> containerResponseList = containerService.getContainerResponses();
        List<ContainerResponse> containerResponseListOnServer = containerService.getContainerResponsesOnServer();
        // then
        Assertions.assertEquals(containerResponseListOnServer.size(), containerResponseList.size(), "서버 컨테이너 개수와 디비에 저장된 컨테이너 개수는 같아야 한다.");
        containerResponseList.forEach(containerResponse -> {
            Assertions.assertNotNull(containerResponse.getContainerId());
            Assertions.assertNotNull(containerResponse.getDockerImageName());
            Assertions.assertNotNull(containerResponse.getLanguageName());
            Assertions.assertNotNull(containerResponse.getLanguageVersion());
            Assertions.assertNotNull(containerResponse.getContainerStatus());
            Assertions.assertNotNull(containerResponse.getContainerState());
            Assertions.assertNotNull(containerResponse.getCodeExecutorType());
            Assertions.assertEquals("running", containerResponse.getContainerState());
            Assertions.assertNotNull(codeExecutorFactory.create(containerService.getContainerById(containerResponse.getId())));
        });
    }

    @Test
    @DisplayName("getContainerById 성공")
    void test_getContainer() {
        // given
        Container findContainer = containerRepository.findAll().get(0);
        // when
        Container container = containerService.getContainerById(findContainer.getId());
        ContainerResponse containerResponse = containerService.getContainerResponse(container.getId());
        // then
        Assertions.assertEquals(container.getId(), findContainer.getId());
        Assertions.assertEquals(containerResponse.getId(), findContainer.getId());
        Assertions.assertEquals(container.getId(), containerResponse.getId());
    }

    @Test
    @DisplayName("getContainerById 실패")
    void test_getContainerById_raise() {
        // given
        Long containerId = 0L;
        // when then
        Assertions.assertThrows(NotFoundException.class, () -> containerService.getContainerById(containerId));
        Assertions.assertThrows(NotFoundException.class, () -> containerService.getContainerResponse(containerId));
    }

    @Test
    @DisplayName("getContainerResponse 성공")
    void test_getContainerResponses() {
        // given
        List<ContainerResponse> containers = containerService.getContainerResponses();
        containers.forEach(containerResponse -> {
            // when
            ContainerResponse response = containerService.getContainerResponse(containerResponse.getId());
            // then
            Assertions.assertNotNull(response.getId());
            Assertions.assertNotNull(response.getContainerId());
            Assertions.assertNotNull(response.getDockerImageName());
            Assertions.assertNotNull(response.getLanguageName());
            Assertions.assertNotNull(response.getLanguageVersion());
            Assertions.assertNotNull(response.getLanguageVersion());
            Assertions.assertNotNull(response.getContainerStatus());
            Assertions.assertNotNull(response.getContainerState());
            Assertions.assertNotNull(response.getContainerPorts());
            Assertions.assertNotNull(response.getCodeExecutorType());
        });
    }

//    @Test
//    @DisplayName("컨테이너 코드 실행 Python 3.8")
//    void 컨테이너_코드_실행_Python_3_8() {
//        // todo action gradle test 에서 에러 발생함
//        // given
//        String[] given = {"python", "3.8", "print(12345)"};
//        // when
//        CodeSnippetResponse codeSnippetResponse = whenExecuteCode(given);
//        // then
//        Assertions.assertEquals("12345\n", codeSnippetResponse.getResponse().get("output"));
//        Assertions.assertEquals("", codeSnippetResponse.getResponse().get("error"));
//    }

    @Test
    @DisplayName("컨테이너 코드 실행 Python 2.7")
    void 컨테이너_코드_실행_Python_2_7() {
        // todo 테스트 코드 리팩토링
        // given
        String[] given = {"python", "2.7", "print(12345)"};
        // when
        CodeSnippetResponse codeSnippetResponse = whenExecuteCode(given);
        CodeSnippetResponse codeSnippetResponse2 = whenRequestExecuteCode(given);
        // then
        Assertions.assertEquals("12345\n", codeSnippetResponse.getResponse().get("output"));
        Assertions.assertEquals("", codeSnippetResponse.getResponse().get("error"));
    }

    @Test
    @DisplayName("컨테이너 코드 실행 PHP 7.4")
    void 컨테이너_코드_실행_PHP_7_4() {
        // given
        String[] given = {"php", "7.4", "<?php\n print_r(['Hello' => 'World']);"};
        // when
        CodeSnippetResponse codeSnippetResponse = whenExecuteCode(given);
        // then
        assertThat(codeSnippetResponse.getResponse().get("output").toString(), containsString("Hello"));
        assertThat(codeSnippetResponse.getResponse().get("output").toString(), containsString("World"));
        Assertions.assertEquals("", codeSnippetResponse.getResponse().get("error"));
    }

    @Test
    @DisplayName("컨테이너 코드 실행 PHP 8.2")
    void 컨테이너_코드_실행_PHP_8_2() {
        // given
        String[] given = {"php", "8.2", "<?php\n print_r(['Hello' => 'World']);"};
        // when
        CodeSnippetResponse codeSnippetResponse = whenExecuteCode(given);
        // then
        assertThat(codeSnippetResponse.getResponse().get("output").toString(), containsString("Hello"));
        assertThat(codeSnippetResponse.getResponse().get("output").toString(), containsString("World"));
        Assertions.assertEquals("", codeSnippetResponse.getResponse().get("error"));
    }

    @Test
    @DisplayName("컨테이너 코드 실행 GCC 4.9")
    void 컨테이너_코드_실행_GCC_4_9() {
        // given
        String[] given = {"gcc", "4.9", "#include <stdio.h>\n\nint main()\n{\n    printf(\"Hello World\");\n    return 0;\n}"};
        // when
        CodeSnippetResponse codeSnippetResponse = whenExecuteCode(given);
        // then
        assertThat(codeSnippetResponse.getResponse().get("output").toString(), containsString("Hello"));
        assertThat(codeSnippetResponse.getResponse().get("output").toString(), containsString("World"));
        Assertions.assertEquals("", codeSnippetResponse.getResponse().get("error"));
    }

    @Test
    @DisplayName("컨테이너 코드 실행 Go 1.19")
    void 컨테이너_코드_실행_GO_1_19() {
        // given
        String[] given = {"go", "1.19", "package main\n\nimport \"fmt\"\n\nfunc main() {\n\tfmt.Println(\"Hello, Go!\")\n}"};
        // when
        CodeSnippetResponse codeSnippetResponse = whenExecuteCode(given);
        // then
        assertThat(codeSnippetResponse.getResponse().get("output").toString(), containsString("Hello"));
        assertThat(codeSnippetResponse.getResponse().get("output").toString(), containsString("Go"));
        Assertions.assertEquals("", codeSnippetResponse.getResponse().get("error"));
    }

    @Test
    @DisplayName("컨테이너 코드 실행 JDK")
    void 컨테이너_코드_실행_JDK() {
        // given
        String[] given = {"jdk", "15", "public class Test {\n    public static void main(String[] args) {\n        System.out.println(\"Hello, Java!\");\n    }\n}"};
        // when
        CodeSnippetResponse codeSnippetResponse = whenExecuteCode(given);
        // then
        assertThat(codeSnippetResponse.getResponse().get("output").toString(), containsString("Hello"));
        assertThat(codeSnippetResponse.getResponse().get("output").toString(), containsString("Java"));
        Assertions.assertEquals("", codeSnippetResponse.getResponse().get("error"));
    }

}