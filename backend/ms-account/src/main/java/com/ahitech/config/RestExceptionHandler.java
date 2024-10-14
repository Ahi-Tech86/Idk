package com.ahitech.config;

import com.ahitech.dtos.ErrorDto;
import com.ahitech.exception.AppException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class RestExceptionHandler {

    @ResponseBody
    @ExceptionHandler(value = {AppException.class})
    public ResponseEntity<ErrorDto> exceptionHandling(AppException exception) {
        return ResponseEntity
                .status(exception.getHttpStatus())
                .body(
                        ErrorDto.builder()
                                .message(exception.getMessage())
                                .build()
                );
    }
}
