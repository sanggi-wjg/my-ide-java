package com.example.myidejava.core.exception.error.code;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum ErrorCode {
    // Not Found
    NOT_FOUND_CONTAINER(HttpStatus.NOT_FOUND.value(), "NOT_FOUND-0001", "컨테이너를 찾을 수 없습니다."),
    NOT_FOUND_MEMBER(HttpStatus.NOT_FOUND.value(), "NOT_FOUND-0002", "유저를 찾을 수 없습니다."),
    NOT_FOUND_CODE_SNIPPET(HttpStatus.NOT_FOUND.value(), "NOT_FOUND-0003", "코드 스니펫을 찾을 수 없습니다."),

    // Docker App Error
    DOCKER_CONTAINER_IS_NOT_HTTP_TYPE(HttpStatus.BAD_REQUEST.value(), "DOCKER-APP-0001", "도커 컨테이너 타입이 HTTP가 아닙니다."),
    DOCKER_CONTAINER_IS_NOT_RUNNING(HttpStatus.BAD_REQUEST.value(), "DOCKER-APP-0002", "도커 컨테이너가 현재 실행중이지 않습니다."),
    DOCKER_CONTAINER_CODE_EXECUTOR_IS_NOT_IMPLEMENTED(HttpStatus.BAD_REQUEST.value(), "DOCKER-APP-0003", "도커 컨테이너 코드 실행 팩토리가 선언되어 있지 않습니다."),
    DOCKER_CONTAINER_FAIL_TO_START_COMMAND(HttpStatus.BAD_REQUEST.value(), "DOCKER-APP-0004", "도커 컨테이너 커맨드 실행 중 에러가 발생 했습니다."),

    // Auth Error
    FAIL_TO_GET_HOST_ADDR(HttpStatus.BAD_REQUEST.value(), "AUTH-0001", "호스트 주소를 불러오는데 실패 하였습니다."),
    FAIL_TO_DECODE_JWT_TOKEN(HttpStatus.UNAUTHORIZED.value(), "AUTH-0002", "JWT Token 디코드 실패 하였습니다."),
    INVALID_AUTHORIZATION_BEARER_HEADER(HttpStatus.UNAUTHORIZED.value(), "AUTH-0003", "Invalid JWT Token in Bearer Header"),
    INVALID_AUTHORIZATION_JWT_TOKEN(HttpStatus.UNAUTHORIZED.value(), "AUTH-0004", "Invalid JWT Token"),
    ALREADY_REGISTERED_USER_EMAIL(HttpStatus.BAD_REQUEST.value(), "AUTH-0005", "이미 등록된 이메일 입니다."),
    INVALID_USER_EMAIL(HttpStatus.BAD_REQUEST.value(), "AUTH-0006", "이메일 확인해주세요."),
    INVALID_USER_EMAIL_OR_PASSWORD(HttpStatus.BAD_REQUEST.value(), "AUTH-0007", "이메일 혹은 비밀번호를 확인해주세요."),

    // Util Error
    FAIL_TO_DECODE_JSON(HttpStatus.BAD_REQUEST.value(), "UTIL-0001", "Json Decode 실패 하였습니다."),
    FAIL_TO_CREATE_FILE(HttpStatus.BAD_REQUEST.value(), "UTIL-0002", "파일 생성에 실패 하였습니다.");

    private final int code;
    private final String codeRule;
    private final String message;

}
