package com.example.myidejava.dto.member;

import com.example.myidejava.domain.member.SocialType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "소셜 로그인 Response")
public class SocialLoginResponse {

    @Schema(description = "소셜 로그인 ID", defaultValue = "1")
    private Long id;
    @Schema(description = "소셜 로그인 타입", defaultValue = "1")
    private SocialType socialType;

    @Schema(description = "유니크 ID", defaultValue = "1")
    private String uniqueId;

    @Schema(description = "토큰", defaultValue = "1")
    private String accessToken;

    @Schema(description = "토큰 발급일", defaultValue = "1")
    private LocalDateTime accessTokenIssuedAt;

    @Schema(description = "토큰 유효시간(분)", defaultValue = "1")
    private Integer accessTokenTtl;

    @Schema(description = "리프레시 토큰", defaultValue = "1")
    private String refreshToken;
}
