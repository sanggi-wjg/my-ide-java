package com.example.myidejava.service.docker;

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


@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@SpringBootTest
class ContainerServiceTest {
    @Autowired
    ContainerService containerService;
    @Autowired
    ContainerRepository containerRepository;
//    @Autowired
//    MyDockerClient myDockerClient;
//    @Autowired
//    AppProperty appProperty;

//    @Test
//    void 컨테이너_이니셜라이즈_확인() {
//        List<String> dockerImageNames = appProperty.getDockerImageNames();
//        dockerImageNames.forEach(dockerImageName -> {
//            String[] split = dockerImageName.split("-");
//            containerRepository.findByLanguageNameAndLanguageVersion(split[1], split[2]).orElseThrow();
//        });
//    }

    @Test
    void 컨테이너_리스트_확인() {
        // when
        List<ContainerResponse> containerResponseOnServerList = containerService.getAllContainersOnServer();
        List<ContainerResponse> containerResponseList = containerService.getAllContainers();
        // then
        Assertions.assertEquals(containerResponseOnServerList.size(), containerResponseList.size(), "서버 컨테이너 개수와 디비에 저장된 컨테이너 개수는 같아야 한다.");
//        Assertions.assertEquals(containerResponseOnServerList.size(), appProperty.getDockerImageNames().size());
//        Assertions.assertEquals(containerResponseList.size(), appProperty.getDockerImageNames().size());
    }

    CodeResponse whenExecuteCode(String langaugeName, String languageVersion, String code) {
        CodeRequest codeRequest = CodeRequest.builder().code(code).build();
        Container container = containerRepository.findByLanguageNameAndLanguageVersion(langaugeName, languageVersion).orElseThrow();
        return containerService.executeCode(container.getId(), codeRequest);
    }

    @Test
    void Python_3_8_컨테이너_코드_실행() {
        // given
        String name = "python";
        String version = "3.8";
        String code = "print(12345)";
        // when
        CodeResponse codeResponse = whenExecuteCode(name, version, code);
        // then
        Assertions.assertEquals("12345\n", codeResponse.getOutput());
        Assertions.assertTrue(codeResponse.getError().isEmpty());
    }

    @Test
    void Python_2_7_컨테이너_코드_실행() {
        // given
        String name = "python";
        String version = "2.7";
        String code = "print(12345)";
        // when
        CodeResponse codeResponse = whenExecuteCode(name, version, code);
        // then
        Assertions.assertEquals("12345\n", codeResponse.getOutput());
        Assertions.assertTrue(codeResponse.getError().isEmpty());
    }

    @Test
    void PHP_7_4_컨테이너_코드_실행() {
        // given
        String name = "php";
        String version = "7.4";
        String code = "<?php\n print_r(['Hello' => 'World']);";
        // when
        CodeResponse codeResponse = whenExecuteCode(name, version, code);
        // then
//        Assertions.assertEquals("12345\n", codeResponse.getOutput());
        Assertions.assertTrue(codeResponse.getError().isEmpty());
    }

}