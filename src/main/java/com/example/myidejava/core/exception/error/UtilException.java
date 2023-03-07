package com.example.myidejava.core.exception.error;

import com.example.myidejava.core.exception.error.code.ErrorCode;
import lombok.Getter;

@Getter
public class UtilException extends RuntimeException {
    private final int errorCode;
    private final String errorCodeRule;
    private final String errorMessage;

    public UtilException(ErrorCode errorCode) {
        this.errorCode = errorCode.getCode();
        this.errorCodeRule = errorCode.getCodeRule();
        this.errorMessage = errorCode.getMessage();
    }
}
