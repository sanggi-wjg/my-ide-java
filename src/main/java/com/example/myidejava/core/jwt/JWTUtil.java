package com.example.myidejava.core.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.myidejava.core.exception.error.AuthException;
import com.example.myidejava.core.exception.error.code.ErrorCode;
import com.example.myidejava.domain.member.Member;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;

@Component
public class JWTUtil {
    @Value("${jwt_secret}")
    private String secret;


    private Algorithm getSignature() {
        return Algorithm.HMAC256(secret);
    }

    private String getIssueHost() {
        try {
            return InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
            throw new AuthException(ErrorCode.FAIL_TO_GET_HOST_ADDR);
        }
    }

    public String generateToken(Member member) {
        return JWT.create()
                .withSubject(JWTPayloadNames.SUBJECT)
                .withClaim(JWTPayloadNames.VERIFIED, true)
                .withClaim(JWTPayloadNames.ID, member.getId())
                .withClaim(JWTPayloadNames.EMAIL, member.getEmail())
                .withClaim(JWTPayloadNames.NAME, member.getUsername())
                .withIssuer(getIssueHost())
                .withIssuedAt(new Date())
                .sign(getSignature());
    }

    public DecodedJWT validateToken(String token) {
        try {
            JWTVerifier verifier = JWT.require(getSignature())
                    .withSubject(JWTPayloadNames.SUBJECT)
                    .withIssuer(getIssueHost())
                    .build();
            return verifier.verify(token);
        } catch (JWTVerificationException e) {
            throw new AuthException(ErrorCode.FAIL_TO_DECODE_JWT_TOKEN);
        }
    }

    public String validateThenRetrieveEmail(String token) {
        DecodedJWT jwt = validateToken(token);
        return jwt.getClaim(JWTPayloadNames.EMAIL).asString();
    }

}

