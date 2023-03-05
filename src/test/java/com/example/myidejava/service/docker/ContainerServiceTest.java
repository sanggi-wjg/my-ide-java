package com.example.myidejava.service.docker;

import com.example.myidejava.module.docker.MyDockerClient;
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
    @Autowired
    MyDockerClient myDockerClient;
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
        Container pythonContainer = containerRepository.findByLanguageNameAndLanguageVersion(langaugeName, languageVersion).orElseThrow();
        return containerService.executeCode(pythonContainer.getId(), codeRequest);
    }

    @Test
    void 파이썬_3_8_실행_가능한_코드_확인() {
        // todo container list 가져와서 foreach 로 모든 컨테이너 코드 실행 테스트로 변경
        // given
        String name = "python";
        String version = "3.8";
        String code = "print(1)";
        // when
        CodeResponse codeResponse = whenExecuteCode(name, version, code);
        // then
        Assertions.assertEquals("1\n", codeResponse.getOutput());
        Assertions.assertTrue(codeResponse.getError().isEmpty());
    }


}