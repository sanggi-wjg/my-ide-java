package com.example.myidejava.core.exception.error.code;

import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ErrorCode {
    // Not Found
    NOT_FOUND_CONTAINER(HttpServletResponse.SC_BAD_REQUEST, "N-0001", "컨테이너를 찾을 수 없습니다."),

    // Docker App Error
    DOCKER_CONTAINER_IS_NOT_HTTP_TYPE(HttpServletResponse.SC_BAD_REQUEST, "D-0001", "도커 컨테이너 타입이 HTTP가 아닙니다."),

    // Auth Error
    FAIL_TO_GET_HOST_ADDR(HttpServletResponse.SC_BAD_REQUEST, "A-0001", "호스트 주소를 불러오는데 실패 하였습니다."),
    FAIL_TO_DECODE_JWT_TOKEN(HttpServletResponse.SC_UNAUTHORIZED, "A-0002", "JWT Token 디코드 실패 하였습니다."),
    INVALID_AUTHORIZATION_BEARER_HEADER(HttpServletResponse.SC_UNAUTHORIZED, "A-0003", "Invalid JWT Token in Bearer Header"),
    INVALID_AUTHORIZATION_JWT_TOKEN(HttpServletResponse.SC_UNAUTHORIZED, "A-0004", "Invalid JWT Token");


    private final int code;
    private final String codeRule;
    private final String message;

}
