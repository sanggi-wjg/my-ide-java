package com.example.myidejava.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "로그인 Request")
public class LoginCredentials {

    @NotBlank
    @Schema(description = "이메일")
    private String email;

    @NotBlank
    @Schema(description = "비밀번호")
    private String password;

}
