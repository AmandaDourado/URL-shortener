package com.example.demo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler extends RuntimeException {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<String> handleResourceNotFoundException(ResourceNotFoundException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ExpiredExceptionHandler.class)
    public ResponseEntity<String> expiredHandlerException(ExpiredExceptionHandler ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.GONE);
    }

    @ExceptionHandler(InvalidKeyException.class)
    public ResponseEntity<String> invalidKeySizeException(InvalidKeyException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(GenerateCodeException.class)
    public ResponseEntity<String> generateCodeException(GenerateCodeException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
