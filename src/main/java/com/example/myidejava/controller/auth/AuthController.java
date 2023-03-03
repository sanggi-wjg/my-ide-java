package com.example.myidejava.controller.auth;

import com.example.myidejava.dto.auth.LoginCredentials;
import com.example.myidejava.dto.auth.LoginResponse;
import com.example.myidejava.dto.auth.RegisterRequest;
import com.example.myidejava.dto.member.MemberResponse;
import com.example.myidejava.service.member.MemberService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
@Tag(name = "인증 API", description = "인증 관련")
public class AuthController {

    private final MemberService memberService;

    @PostMapping("/register")
    @ApiResponse(responseCode = "201", description = "이메일 유저 등록")
    public ResponseEntity<MemberResponse> register(@RequestBody @Valid RegisterRequest registerRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(memberService.registerEmailUser(registerRequest));
    }

    @PostMapping("/login")
    @ApiResponse(responseCode = "200", description = "로그인")
    public ResponseEntity<LoginResponse> login(@RequestBody @Valid LoginCredentials loginCredentials) {
        return ResponseEntity.ok(memberService.authenticate(loginCredentials));
    }
}
