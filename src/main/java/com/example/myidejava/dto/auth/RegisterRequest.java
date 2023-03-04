package com.example.myidejava.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "유저 등록 Request")
public class RegisterRequest {
    @Schema(description = "이메일", defaultValue = "user@dev.com")
    @NotEmpty
    private String email;

    @Schema(description = "이름", defaultValue = "John Doe")
    @NotEmpty
    private String username;

    @Schema(description = "비밀번호", defaultValue = "passw0rd")
    @NotEmpty
    private String password;
}
