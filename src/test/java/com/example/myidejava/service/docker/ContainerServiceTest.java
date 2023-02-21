package com.example.myidejava.service.docker;

import com.example.myidejava.core.AppProperty;
import com.example.myidejava.core.util.docker.MyDockerClient;
import com.example.myidejava.repository.docker.ContainerRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

//class FakeMyDockerClient extends MyDockerClient {
//
//    @Override
//    public List<ContainerResponse> getAllContainers() {
//
//    }
//
//}

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

    @Autowired
    AppProperty appProperty;

    @Test
    void 테스트_1() throws Exception {
        // given
        // when
//        containerService.initialize();
        // then
        List<String> dockerImageNames = appProperty.getDockerImageNames();
        dockerImageNames.forEach(dockerImageName -> {
            String[] split = dockerImageName.split("-");
            containerRepository.findByLanguageNameAndLanguageVersion(split[1], split[2]).orElseThrow();
        });
    }

}