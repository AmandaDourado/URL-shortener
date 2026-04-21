package com.example.demo.exception;

public class InvalidKeyException extends RuntimeException {

    public InvalidKeyException(String message) {
        super(message);
    }
}
