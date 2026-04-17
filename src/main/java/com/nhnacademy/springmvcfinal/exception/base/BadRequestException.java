package com.nhnacademy.springmvcfinal.exception.base;

public class BadRequestException extends RuntimeException {
    public BadRequestException(String message) {
        super("[BadRequest] " + message);
    }
}
