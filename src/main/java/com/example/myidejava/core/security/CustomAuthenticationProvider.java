package com.example.myidejava.core.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CustomAuthenticationProvider implements AuthenticationProvider {
    private final MyUserDetailService userDetailService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String email = authentication.getName();
        String password = (String) authentication.getCredentials();

        UserDetails user = userDetailService.loadUserByUsername(email);
        if (user == null){
            throw new BadCredentialsException("todo invalid email");
        }
        if (!passwordEncoder.matches(password, user.getPassword())){
            throw new BadCredentialsException("todo invalid email or password");
        }
        return new CustomAuthenticationToken(user.getUsername(), user.getPassword(), user.getAuthorities());

    }

    @Override
    public boolean supports(Class<?> authentication) {
        return CustomAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
