package com.example.myidejava.dto.docker;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Schema(description = "코드 실행 Response")
public class RunCodeResponse {

    @Schema(description = "코드 실행 결과", defaultValue = "1")
    private String output;

    @Schema(description = "코드 실행 에러", defaultValue = "1")
    private String error;

}
