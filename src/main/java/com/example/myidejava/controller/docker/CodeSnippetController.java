package com.example.myidejava.controller.docker;

import com.example.myidejava.dto.docker.CodeSnippetResponse;
import com.example.myidejava.dto.docker.CodeSnippetSearch;
import com.example.myidejava.dto.docker.CodeSnippetSearchResponse;
import com.example.myidejava.service.docker.CodeSnippetService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;


@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@Tag(name = "코드 스니펫 API", description = "코드 실행 정보 관련")
public class CodeSnippetController {
    private final CodeSnippetService codeSnippetService;

    @GetMapping("/code-snippets")
    @ApiResponse(responseCode = "200", description = "모든 코드 실행 정보")
    public ResponseEntity<CodeSnippetSearchResponse> codeSnippets(
            @Valid CodeSnippetSearch codeSnippetSearch,
            @PageableDefault(size = 20) @SortDefault(sort = "id", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        return ResponseEntity.ok(codeSnippetService.getCodeSnippetSearchResponse(codeSnippetSearch, pageable));
    }

    @GetMapping("/code-snippets/{code_snippet_id}")
    @ApiResponse(responseCode = "200", description = "코드 실행 정보")
    public ResponseEntity<CodeSnippetResponse> codeSnippetById(
            @PathVariable("code_snippet_id") Long codeSnippetId
    ) {
        return ResponseEntity.ok(codeSnippetService.getCodeSnippetResponse(codeSnippetId));
    }

}
