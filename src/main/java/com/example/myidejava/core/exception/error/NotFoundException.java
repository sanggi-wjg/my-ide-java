package com.example.myidejava.core.exception.error;

import lombok.Getter;

@Getter
public class NotFoundException extends RuntimeException {

    private final int errorCode;
    private final String errorCodeRule;
    private final String errorMessage;

    public NotFoundException(ErrorCode errorCode) {
        this.errorCode = errorCode.getCode();
        this.errorCodeRule = errorCode.getCodeRule();
        this.errorMessage = errorCode.getMessage();
    }
}
