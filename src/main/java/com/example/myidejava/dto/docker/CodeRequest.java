package com.example.myidejava.dto.docker;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor
@Schema(description = "코드 실행 Request")
public class CodeRequest {

    @NotBlank
    @Schema(description = "실행할 코드", defaultValue = "print([i for i in range(10)])")
    private String code;

}
