package com.example.myidejava.controller.container;

import com.example.myidejava.core.util.MyDockerClient;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
@Tag(name = "도커 컨테이너", description = "")
public class ContainerController {


    private final MyDockerClient dockerClient;

    @GetMapping("/docker/containers")
    @ApiResponse(responseCode = "200")
    public ResponseEntity<String> getContainers() {
        dockerClient.getContainers();

        return ResponseEntity.ok().body("Hello containers");
    }

}
