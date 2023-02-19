package com.example.myidejava.dto.docker;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Map;

@Getter
@Setter
@Builder
@AllArgsConstructor
@Schema(description = "코드 스니펫 Response")
public class CodeSnippetResponse {

    @Schema(description = "code_snippet_id", defaultValue = "1")
    private Long id;

    @Schema(description = "code request", defaultValue = "1")
    private String request;

    @Schema(description = "code response", defaultValue = "1")
    private Map<String, Object> response;

    @Schema(description = "생성일", defaultValue = "1")
    private LocalDateTime createdAt;

}
