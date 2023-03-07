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

    @Schema(description = "code snippet id", defaultValue = "1")
    private Long id;

    @Schema(description = "code request", defaultValue = "print(1)")
    private String request;

    @Schema(description = "code response", defaultValue = ".")
    private Map<String, Object> response;

    @Schema(description = "생성일", defaultValue = "1")
    private LocalDateTime createdAt;

    @Schema(description = "성공 여부", defaultValue = "True")
    private Boolean isSuccess;
}
