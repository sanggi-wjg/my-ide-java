package com.example.myidejava.core.jwt;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.example.myidejava.core.common.CommonConstants;
import com.example.myidejava.core.security.MyUserDetailService;
import com.example.myidejava.core.exception.error.code.ErrorCode;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JWTFilter extends OncePerRequestFilter {

    private final MyUserDetailService userDetailService;
    private final JWTUtil jwtUtil;

    private boolean isValidAuthorization(String authorization) {
        return authorization != null &&
                !authorization.isBlank() &&
                authorization.startsWith(CommonConstants.BEARER_WITH_SPACE);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authorization = request.getHeader(CommonConstants.AUTHORIZATION);
        if (isValidAuthorization(authorization)) {
            String jwt = authorization.substring(CommonConstants.JWT_TOKEN_INDEX);
            if (jwt.isBlank() || jwt.isEmpty()) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, ErrorCode.INVALID_AUTHORIZATION_BEARER_HEADER.getMessage());

            } else {
                try {
                    String email = jwtUtil.validateThenRetrieveEmail(jwt);
                    UserDetails userDetails = userDetailService.loadUserByUsername(email);
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(email, userDetails.getPassword(), userDetails.getAuthorities());

                    if (SecurityContextHolder.getContext().getAuthentication() == null) {
                        SecurityContextHolder.getContext().setAuthentication(authToken);
                    }
                } catch (JWTVerificationException exc) {
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, ErrorCode.INVALID_AUTHORIZATION_JWT_TOKEN.getMessage());
                }
            }
        }
        filterChain.doFilter(request, response);
    }
}
