package com.example.demo.exception;

public class ExpiredExceptionHandler extends RuntimeException {

    public ExpiredExceptionHandler(String message) {
        super(message);
    }
}
