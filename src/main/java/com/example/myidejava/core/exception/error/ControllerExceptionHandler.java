package com.example.myidejava.core.exception.error;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
@RequiredArgsConstructor
public class ControllerExceptionHandler extends ResponseEntityExceptionHandler {

    private ResponseEntity<Object> response(MyException e) {
        return new ResponseEntity<>(e, null, e.getErrorCode());
    }

    @ExceptionHandler(NotFoundException.class)
    protected ResponseEntity<Object> handleNotFoundException(NotFoundException e) {
        return response(MyException.builder()
                .errorCode(e.getErrorCode())
                .errorCodeRule(e.getErrorCodeRule())
                .errorMessage(e.getErrorMessage())
                .build()
        );
    }

}
