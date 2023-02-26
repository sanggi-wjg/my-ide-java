package com.example.myidejava.core.exception.error.code;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ErrorCode {
    NOT_FOUND_CONTAINER(404, "N-0001", "컨테이너를 찾을 수 없습니다."),

    DOCKER_CONTAINER_IS_NOT_HTTP_TYPE(400, "D-0001", "도커 컨테이너 타입이 HTTP가 아닙니다.");

    private final int code;
    private final String codeRule;
    private final String message;

}
