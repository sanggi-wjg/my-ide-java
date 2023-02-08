package com.example.myidejava.controller.docker;

import com.example.myidejava.core.util.MyDockerClient;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("dockers")
@RequiredArgsConstructor
@Tag(name = "Docker API", description = "Docker 관련 API")
public class ContainerController {
    private final MyDockerClient dockerClient;

    @GetMapping("/containers")
    @ApiResponse(responseCode = "200", description = "도커 컨테이너 리스트")
    public ResponseEntity<String> getContainers() {
        dockerClient.getContainers();
        return ResponseEntity.ok().body("Hello containers");
    }

}
