package com.example.myidejava.dto.member;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "유저 등록 Response")
public class MemberResponse {

    @Schema(description = "유저 ID", defaultValue = "1")
    private Long id;

    @Schema(description = "이메일", defaultValue = "user@dev.com")
    private String email;

    @Schema(description = "이름", defaultValue = "John Doe")
    private String username;

    @Schema(description = "활성 여부", defaultValue = "true")
    private Boolean isActive;

    @Schema(description = "소셜 로그인 서비스", defaultValue = "array")
    private List<SocialLoginResponse> socialLoginResponseList;

}
