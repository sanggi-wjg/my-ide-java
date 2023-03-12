package com.example.myidejava.dto.docker;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@Schema(description = "코드 스니펫 검색 Response")
public class CodeSnippetSearchResponse {
    private Long totalCount;
    private Integer totalPage;
    private Integer currentCount;
    private Boolean hasPrev;
    private Boolean hasNext;
    private List<CodeSnippetResponse> codeSnippetResponses;
}
