package com.example.myidejava.controller.docker;

import com.example.myidejava.dto.docker.CodeRequest;
import com.example.myidejava.dto.docker.CodeSnippetResponse;
import com.example.myidejava.service.docker.ContainerService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@RestController
@RequestMapping("/api/v2")
@RequiredArgsConstructor
@Tag(name = "도커 컨테이너 API", description = "도커 컨테이너 관련")
public class ContainerKafkaController {
    private final ContainerService containerService;

    @PostMapping("/containers/{container_id}/code-snippets")
    @ApiResponse(responseCode = "202", description = "도커 컨테이너 안에서 코드 실행")
    public ResponseEntity<CodeSnippetResponse> executeCodeOnContainer(
            @PathVariable("container_id") Long containerId,
            @RequestBody @Valid CodeRequest codeRequest,
            Authentication authentication
    ) {
        CodeSnippetResponse codeSnippetResponse = containerService.requestCodeSnippetToExecute(containerId, codeRequest, authentication);
        return ResponseEntity.accepted().body(codeSnippetResponse);
    }


}
