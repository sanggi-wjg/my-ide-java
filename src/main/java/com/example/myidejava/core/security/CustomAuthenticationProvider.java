package com.example.myidejava.core.security;

import com.example.myidejava.core.exception.error.AuthException;
import com.example.myidejava.core.exception.error.code.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CustomAuthenticationProvider implements AuthenticationProvider {
    private final UserDetailServiceImpl userDetailService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String email = authentication.getName();
        String password = (String) authentication.getCredentials();

        UserDetails user = userDetailService.loadUserByUsername(email);
        if (user == null) {
            throw new AuthException(ErrorCode.INVALID_USER_EMAIL);
        }
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new AuthException(ErrorCode.INVALID_USER_EMAIL_OR_PASSWORD);
        }
        return new CustomAuthenticationToken(user.getUsername(), user.getPassword(), user.getAuthorities());

    }

    @Override
    public boolean supports(Class<?> authentication) {
        return CustomAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
