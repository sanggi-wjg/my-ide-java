package com.example.myidejava.core.exception.error.code;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum ErrorCode {
    // Not Found
    NOT_FOUND_CONTAINER(HttpStatus.BAD_REQUEST.value(), "N-0001", "컨테이너를 찾을 수 없습니다."),

    // Docker App Error
    DOCKER_CONTAINER_IS_NOT_HTTP_TYPE(HttpStatus.BAD_REQUEST.value(), "D-0001", "도커 컨테이너 타입이 HTTP가 아닙니다."),
    DOCKER_CONTAINER_IS_NOT_RUNNING(HttpStatus.BAD_REQUEST.value(), "D-0002", "도커 컨테이너가 현재 실행중이지 않습니다."),

    // Auth Error
    FAIL_TO_GET_HOST_ADDR(HttpStatus.BAD_REQUEST.value(), "A-0001", "호스트 주소를 불러오는데 실패 하였습니다."),
    FAIL_TO_DECODE_JWT_TOKEN(HttpStatus.UNAUTHORIZED.value(), "A-0002", "JWT Token 디코드 실패 하였습니다."),
    INVALID_AUTHORIZATION_BEARER_HEADER(HttpStatus.UNAUTHORIZED.value(), "A-0003", "Invalid JWT Token in Bearer Header"),
    INVALID_AUTHORIZATION_JWT_TOKEN(HttpStatus.UNAUTHORIZED.value(), "A-0004", "Invalid JWT Token");


    private final int code;
    private final String codeRule;
    private final String message;

}
