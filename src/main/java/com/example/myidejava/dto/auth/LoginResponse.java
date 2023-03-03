package com.example.myidejava.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "로그인 Response")
public class LoginResponse {

    @Schema(description = "토큰", defaultValue = "1")
    private String token;

}
