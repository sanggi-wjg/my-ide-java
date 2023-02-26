package com.example.myidejava.core.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ErrorResponse {
    private int errorCode;
    private final String errorCodeRule;
    private String errorMessage;
}
