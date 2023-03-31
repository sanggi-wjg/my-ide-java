package com.example.myidejava.controller.docker;

import com.example.myidejava.core.common.CommonConstants;
import com.example.myidejava.dto.docker.CodeRequest;
import com.example.myidejava.dto.docker.CodeSnippetResponse;
import com.example.myidejava.dto.docker.ContainerResponse;
import com.example.myidejava.service.docker.ContainerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;


@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@Tag(name = "도커 컨테이너 API", description = "도커 컨테이너 관련")
public class ContainerController {
    private final ContainerService containerService;

    @GetMapping("/containers")
    @ApiResponse(responseCode = "200", description = "도커 컨테이너 리스트")
    public ResponseEntity<List<ContainerResponse>> containers() {
        return ResponseEntity.ok(containerService.getContainerResponses());
    }

    @GetMapping("/containers/on-server")
    @Operation(security = {@SecurityRequirement(name = CommonConstants.SWAGGER_AUTHORIZE_NAME)})
    @ApiResponse(responseCode = "200", description = "서버 도커 컨테이너 리스트")
    public ResponseEntity<List<ContainerResponse>> containersOnServer(Authentication authentication) {
        return ResponseEntity.ok(containerService.getContainerResponsesOnServer());
    }

    @GetMapping("/containers/{container_id}")
    @ApiResponse(responseCode = "200", description = "도커 컨테이너 정보")
    public ResponseEntity<ContainerResponse> container(
            @PathVariable("container_id") Long containerId
    ) {
        return ResponseEntity.ok(containerService.getContainerResponse(containerId));
    }

    @PostMapping("/containers/{container_id}/code")
    @ApiResponse(responseCode = "201", description = "도커 컨테이너 안에서 코드 실행")
    public ResponseEntity<CodeSnippetResponse> executeCodeOnContainer(
            @PathVariable("container_id") Long containerId,
            @RequestBody @Valid CodeRequest codeRequest,
            Authentication authentication
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(containerService.executeCode(containerId, codeRequest, authentication));
    }

}
