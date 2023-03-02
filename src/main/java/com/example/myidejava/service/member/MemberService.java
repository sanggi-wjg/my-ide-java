package com.example.myidejava.service.member;

import com.example.myidejava.core.auth.JWTUtil;
import com.example.myidejava.domain.member.Member;
import com.example.myidejava.domain.member.SocialLogin;
import com.example.myidejava.dto.auth.RegisterRequest;
import com.example.myidejava.repository.member.MemberRepository;
import com.example.myidejava.repository.member.SocialLoginRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final SocialLoginRepository socialLoginRepository;
    private final JWTUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    public void registerEmailUser(RegisterRequest registerRequest) {
        validateEmail(registerRequest.getEmail());
        Member member = Member.createEmailUser(
                registerRequest.getEmail(),
                registerRequest.getUsername(),
                passwordEncoder.encode(registerRequest.getPassword())
        );
        SocialLogin socialLogin = SocialLogin.createEmail(member, jwtUtil.generateToken(member));
        memberRepository.save(member);
        socialLoginRepository.save(socialLogin);
    }

    @Transactional(readOnly = true)
    public void validateEmail(String email) {
        memberRepository.findByEmail(email).ifPresent(member -> {
            throw new IllegalStateException("todo: already registered");
        });
    }

}
