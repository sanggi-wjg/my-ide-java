package com.example.myidejava.core.exception.error;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class MyError {
    private int errorCode;
    private final String errorCodeRule;
    private String errorMessage;
}
