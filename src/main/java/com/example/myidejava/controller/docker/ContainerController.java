package com.example.myidejava.controller.docker;

import com.example.myidejava.dto.docker.ContainerDto;
import com.example.myidejava.dto.docker.RunCodeRequest;
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
    public ResponseEntity<List<ContainerDto>> getContainers() {
        List<ContainerDto> containers = containerService.getAllContainers();
        return ResponseEntity.ok().body(containers);
    }

    @PostMapping("/containers/{container_id}")
    @ApiResponse(responseCode = "200", description = "컨네이너 코드 실행")
    public ResponseEntity<String> runCodeOnContainer(
            @PathVariable("container_id") Long containerId,
            @RequestBody @Valid RunCodeRequest runCodeRequest
    ) {
        return ResponseEntity.ok().body(runCodeRequest.getCode());
    }


}
