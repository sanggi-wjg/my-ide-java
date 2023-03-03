package com.example.myidejava.service.member;

import com.example.myidejava.core.security.CustomAuthenticationProvider;
import com.example.myidejava.core.jwt.JWTUtil;
import com.example.myidejava.domain.member.Member;
import com.example.myidejava.domain.member.SocialLogin;
import com.example.myidejava.dto.auth.LoginCredentials;
import com.example.myidejava.dto.auth.LoginResponse;
import com.example.myidejava.dto.auth.RegisterRequest;
import com.example.myidejava.dto.member.MemberResponse;
import com.example.myidejava.mapper.MemberMapper;
import com.example.myidejava.repository.member.MemberRepository;
import com.example.myidejava.repository.member.SocialLoginRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Valid;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final SocialLoginRepository socialLoginRepository;
    private final JWTUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;
    private final CustomAuthenticationProvider authenticationManager;
    private final MemberMapper memberMapper;

    public MemberResponse registerEmailUser(RegisterRequest registerRequest) {
        validateEmail(registerRequest.getEmail());

        Member member = Member.createEmailUser(registerRequest.getEmail(), registerRequest.getUsername(), passwordEncoder.encode(registerRequest.getPassword()));
        SocialLogin socialLogin = SocialLogin.createEmail(member, jwtUtil.generateToken(member));
        memberRepository.save(member);
        socialLoginRepository.save(socialLogin);
        return memberMapper.INSTANCE.toRegisterResponse(member, member.getSocialLoginList());
    }

    public LoginResponse authenticate(@Valid LoginCredentials loginCredentials) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginCredentials.getEmail(), loginCredentials.getPassword()));
        Member member = memberRepository.findByEmail(loginCredentials.getEmail()).orElseThrow(() -> {
            throw new IllegalStateException("todo : not found member entity");
        });
        String token = jwtUtil.generateToken(member);
        return LoginResponse.builder().token(token).build();
    }

    @Transactional(readOnly = true)
    public void validateEmail(String email) {
        memberRepository.findByEmail(email).ifPresent(member -> {
            throw new IllegalStateException("todo: already registered");
        });
    }

}
