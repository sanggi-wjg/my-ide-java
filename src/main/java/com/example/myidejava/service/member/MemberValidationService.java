package com.example.myidejava.service.member;

import com.example.myidejava.core.exception.error.AuthException;
import com.example.myidejava.core.exception.error.code.ErrorCode;
import com.example.myidejava.repository.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberValidationService {
    private final MemberRepository memberRepository;

    public void validateEmail(String email) {
        memberRepository.findByEmail(email).ifPresent(member -> {
            throw new AuthException(ErrorCode.ALREADY_REGISTERED_USER_EMAIL);
        });
    }

}
