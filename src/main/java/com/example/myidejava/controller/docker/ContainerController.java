package com.example.myidejava.controller.docker;

import com.example.myidejava.dto.docker.CodeResponse;
import com.example.myidejava.dto.docker.ContainerResponse;
import com.example.myidejava.dto.docker.CodeRequest;
import com.example.myidejava.service.docker.ContainerService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;


@RestController
@RequestMapping("dockers")
@RequiredArgsConstructor
@Tag(name = "Docker API", description = "Docker 관련 API")
public class ContainerController {
    private final ContainerService containerService;

    @GetMapping("/containers")
    @ApiResponse(responseCode = "200", description = "도커 컨테이너 리스트")
    public ResponseEntity<List<ContainerResponse>> getContainers() {
        return ResponseEntity.ok(containerService.getAllContainers());
    }

    @GetMapping("/containers/on-server")
    @ApiResponse(responseCode = "200", description = "서버 도커 컨테이너 리스트")
    public ResponseEntity<List<ContainerResponse>> getContainersOnServer() {
        return ResponseEntity.ok(containerService.getAllContainersOnServer());
    }

    @PostMapping("/containers/{container_id}")
    @ApiResponse(responseCode = "200", description = "도커 컨테이너 안에서 코드 실행")
    public ResponseEntity<CodeResponse> executeCodeOnContainer(
            @PathVariable("container_id") Long containerId,
            @RequestBody @Valid CodeRequest codeRequest
    ) {
        return ResponseEntity.ok(containerService.executeCode(containerId, codeRequest));
    }


}
