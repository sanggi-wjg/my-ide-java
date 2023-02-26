package com.example.myidejava.core.exception;

import com.example.myidejava.core.exception.error.DockerAppException;
import com.example.myidejava.core.exception.error.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
@RequiredArgsConstructor
public class ControllerExceptionHandler extends ResponseEntityExceptionHandler {

    private ResponseEntity<Object> buildError(ErrorResponse error) {
        return new ResponseEntity<>(error, null, error.getErrorCode());
    }

    @ExceptionHandler(NotFoundException.class)
    protected ResponseEntity<Object> handleNotFoundException(NotFoundException e) {
        return buildError(new ErrorResponse(e.getErrorCode(), e.getErrorCodeRule(), e.getErrorMessage()));
    }

    @ExceptionHandler(DockerAppException.class)
    protected ResponseEntity<Object> handleDockerAppException(DockerAppException e) {
        return buildError(new ErrorResponse(e.getErrorCode(), e.getErrorCodeRule(), e.getErrorMessage()));
    }

}
