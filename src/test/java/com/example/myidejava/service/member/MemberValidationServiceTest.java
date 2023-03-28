package com.example.myidejava.service.member;

import com.example.myidejava.core.exception.error.AuthException;
import com.example.myidejava.dto.auth.RegisterRequest;
import com.example.myidejava.dto.member.MemberResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@SpringBootTest
class MemberValidationServiceTest {
    @Autowired
    MemberService memberService;
    @Autowired
    MemberValidationService memberValidationService;

    @Test
    @DisplayName("validateEmail")
    void test_validateEmail() {
        // given
        String email = "dev@dev.com";
        // when then
        memberValidationService.validateEmail(email);
    }

    @Test
    @DisplayName("validateEmail Failure")
    void test_validateEmail_raise() {
        // given
        String email = "dev@dev.com";
        RegisterRequest registerRequest = RegisterRequest.builder().email(email).username(email).password("passw0rd").build();
        // when
        MemberResponse memberResponse = memberService.createEmailUser(registerRequest);
        // then
        Assertions.assertThrows(AuthException.class, () -> memberValidationService.validateEmail(memberResponse.getEmail()));
    }

}