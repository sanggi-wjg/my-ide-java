package com.example.myidejava.core.exception.error;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ErrorCode {
    NOT_FOUND_CONTAINER(404, "1000", "컨테이너를 찾을 수 없습니다.");

    private final int code;
    private final String codeRule;
    private final String message;

}
