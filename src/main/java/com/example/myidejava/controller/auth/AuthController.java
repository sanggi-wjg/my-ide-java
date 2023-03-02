package com.example.myidejava.controller.auth;

import com.example.myidejava.dto.auth.RegisterRequest;
import com.example.myidejava.service.member.MemberService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@Tag(name = "인증 API", description = "인증 관련")
public class AuthController {

    private final MemberService memberService;

    @PostMapping("/auth/register")
    @ApiResponse(responseCode = "201", description = "이메일 유저 등록")
    public ResponseEntity<String> register(@RequestBody @Valid RegisterRequest registerRequest) {
        memberService.registerEmailUser(registerRequest);
        return ResponseEntity.ok("123");
    }

}
