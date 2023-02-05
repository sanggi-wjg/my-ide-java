package com.example.myidejava.controller.container;

import de.gesellix.docker.client.DockerClientImpl;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
@RequiredArgsConstructor
@Tag(name = "도커 컨테이너 API", description = "도커 관련")
public class ContainerController {

    @Value("${DOCKER_HOST}") // 변수 파일에 등록된 java.file.test 값 가져오기
    String dockerHost;

    @GetMapping("/containers")
    public ResponseEntity<String> getContainers() {
//        DockerClientImpl dockerHost = new DockerClientImpl(this.dockerHost);
        return ResponseEntity.ok().body("Hello containers");
    }

}
