package com.example.myidejava.core.exception.error;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class MyException {
    private int errorCode;
    private final String errorCodeRule;
    private String errorMessage;
}
