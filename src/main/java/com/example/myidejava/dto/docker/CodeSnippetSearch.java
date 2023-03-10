package com.example.myidejava.dto.docker;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Nullable;
import lombok.*;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "코드 스니펫 검색")
public class CodeSnippetSearch {
    @Nullable
    @Schema(description = "코드 실행 요청", defaultValue = "print(1)")
    private String request;

    @Nullable
    @Min(value = 0)
    @Max(value = 1)
    @Schema(description = "코드 실행 성공 여부", defaultValue = "0,1")
    private Integer isSuccess;

}
