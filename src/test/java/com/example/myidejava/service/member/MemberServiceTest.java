package com.example.myidejava.service.member;

import com.example.myidejava.core.exception.error.AuthException;
import com.example.myidejava.core.jwt.JWTUtil;
import com.example.myidejava.dto.auth.RegisterRequest;
import com.example.myidejava.dto.member.MemberResponse;
import com.example.myidejava.module.util.FileUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@SpringBootTest
class MemberServiceTest {
    @Autowired
    MemberService memberService;
    @Autowired
    MemberValidationService memberValidationService;
    @Autowired
    JWTUtil jwtUtil;

    @Test
    @DisplayName("createEmailUser")
    void test_createEmailUser() {
        // given
        String email =  UUID.randomUUID().toString();
        RegisterRequest registerRequest = RegisterRequest.builder().email(email).username(email).password("passw0rd").build();
        // when
        MemberResponse memberResponse = memberService.createEmailUser(registerRequest);
        // then
        assertNotNull(memberResponse.getId());
        assertEquals(email, memberResponse.getEmail());
        assertEquals(email, memberResponse.getUsername());
        assertTrue(memberResponse.getIsActive());
        memberResponse.getSocialLoginResponses().forEach(socialLoginResponse -> {
            String accessToken = socialLoginResponse.getAccessToken();
            jwtUtil.validateToken(accessToken);
            assertEquals(email, jwtUtil.validateThenRetrieveEmail(accessToken));
        });
    }

    @Test
    @DisplayName("validateEmail")
    void test_validateEmail() {
        // given
        String email =  UUID.randomUUID().toString();
        // when then
        memberValidationService.validateEmail(email);
    }

    @Test
    @DisplayName("validateEmail Failure")
    void test_validateEmail_raise() {
        // given
        String email =  UUID.randomUUID().toString();
        RegisterRequest registerRequest = RegisterRequest.builder().email(email).username(email).password("passw0rd").build();
        // when
        MemberResponse memberResponse = memberService.createEmailUser(registerRequest);
        // then
        Assertions.assertThrows(AuthException.class, () -> memberValidationService.validateEmail(memberResponse.getEmail()));
    }

}