package com.example.myidejava.controller.docker;

import com.example.myidejava.dto.docker.*;
import com.example.myidejava.service.docker.ContainerService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
        return ResponseEntity.ok(containerService.getAllContainers());
    }

    @GetMapping("/containers/on-server")
    @ApiResponse(responseCode = "200", description = "서버 도커 컨테이너 리스트")
    public ResponseEntity<List<ContainerResponse>> containersOnServer() {
        return ResponseEntity.ok(containerService.getAllContainersOnServer());
    }

    @GetMapping("/containers/code-snippets")
    @ApiResponse(responseCode = "200", description = "모든 코드 실행 정보")
    public ResponseEntity<CodeSnippetSearchResponse> codeSnippets(
            @PageableDefault(size = 20) @SortDefault(sort = "id", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        return ResponseEntity.ok(containerService.getCodeSnippets(pageable));
    }

    @GetMapping("/containers/{container_id}/code-snippets")
    @ApiResponse(responseCode = "200", description = "도커 컨테이너 코드 실행 정보")
    public ResponseEntity<CodeSnippetSearchResponse> codeSnippetsByContainerId(
            @PathVariable("container_id") Long containerId,
            @Valid CodeSnippetSearch codeSnippetSearch,
            @PageableDefault(size = 20) @SortDefault(sort = "id", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        return ResponseEntity.ok(containerService.getCodeSnippetsByContainerId(containerId, codeSnippetSearch,pageable));
    }

    @PostMapping("/containers/{container_id}/code-snippets")
    @ApiResponse(responseCode = "201", description = "도커 컨테이너 안에서 코드 실행")
    public ResponseEntity<CodeResponse> executeCodeOnContainer(
            @PathVariable("container_id") Long containerId,
            @RequestBody @Valid CodeRequest codeRequest
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(containerService.executeCode(containerId, codeRequest));
    }


}
