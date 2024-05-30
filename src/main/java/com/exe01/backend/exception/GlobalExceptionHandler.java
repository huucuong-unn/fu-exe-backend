package com.exe01.backend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BaseException.class)
    public ResponseEntity<ErrorResponse> handleBaseException(BaseException ex) {
        System.out.println("Handling BaseException with message: " + ex.getErrorMessage());
        ErrorResponse errorResponse = new ErrorResponse(
                ex.getErrorCode(),
                ex.getErrorMessage(),
                ex.getErrorStatus()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.valueOf(ex.getErrorCode()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception ex) {
        System.out.println("Handling Exception with message: " + ex.getMessage());
        ErrorResponse errorResponse = new ErrorResponse(
                500,
                ex.getMessage(),
                "Internal Server Error"
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}