package com.example.IvasAPI.exception;

public class InvalidBINException extends RuntimeException {
    public InvalidBINException(String message) {
        super(message);
    }
}
