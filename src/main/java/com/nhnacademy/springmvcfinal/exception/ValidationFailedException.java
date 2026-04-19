package com.nhnacademy.springmvcfinal.exception;

public class ValidationFailedException extends RuntimeException {
    public ValidationFailedException() {
        super("validation failed");
    }
}
