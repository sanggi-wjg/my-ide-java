package com.example.myidejava.service.docker;

import com.example.myidejava.core.exception.error.NotFoundException;
import com.example.myidejava.domain.docker.Container;
import com.example.myidejava.dto.docker.*;
import com.example.myidejava.repository.docker.ContainerRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
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

    CodeSnippetResponse whenExecuteCode(String[] given) {
        CodeRequest codeRequest = CodeRequest.builder().code(given[2]).build();
        Container container = containerRepository.findByLanguageNameAndLanguageVersion(given[0], given[1]).orElseThrow();
        return containerService.executeCode(container.getId(), codeRequest, null);
    }

    @Test
    void test_initialize() {
        // when
        List<ContainerResponse> containerResponseListOnServer = containerService.getContainersOnServer();
        List<ContainerResponse> containerResponseList = containerService.getContainers();
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
        });
    }

    @Test
    void test_getContainerById() {
        Container findContainer = containerRepository.findAll().get(0);
        Container container = containerService.getContainerById(findContainer.getId());
        Assertions.assertEquals(container.getId(), findContainer.getId());
    }

    @Test
    void test_getContainerById_raise() {
        Long containerId = 99999L;
        Assertions.assertThrows(NotFoundException.class, () -> containerService.getContainerById(containerId));
    }

    @Test
    void test_getCodeSnippetsByContainerId() {
        // todo refactoring
        // given
        List<ContainerResponse> containers = containerService.getContainers();
        // when then
        containers.forEach(containerResponse -> {
            CodeSnippetSearchResponse codeSnippetSearchResponse = codeSnippetService.getCodeSnippetsBySearch(
                    new CodeSnippetSearch(1L,"print", 1),
                    PageRequest.of(0, 5)
            );
            Assertions.assertNotNull(codeSnippetSearchResponse.getCodeSnippetResponses());
        });
    }

//    @Test
//    void 컨테이너_코드_실행_Python_3_8() {
//      todo action gradle test 에서 에러 발생함
//        // given
//        String[] given = {"python", "3.8", "print(12345)"};
//        // when
//        CodeSnippetResponse codeSnippetResponse = whenExecuteCode(given);
//        // then
//        Assertions.assertEquals("12345\n", codeSnippetResponse.getResponse().get("output"));
//        Assertions.assertTrue(codeSnippetResponse.getError().isEmpty());
//    }

    @Test
    void 컨테이너_코드_실행_Python_2_7() {
        // given
        String[] given = {"python", "2.7", "print(12345)"};
        // when
        CodeSnippetResponse codeSnippetResponse = whenExecuteCode(given);
        // then
        Assertions.assertEquals("12345\n", codeSnippetResponse.getResponse().get("output"));
        Assertions.assertEquals("", codeSnippetResponse.getResponse().get("error"));
    }

    @Test
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

}