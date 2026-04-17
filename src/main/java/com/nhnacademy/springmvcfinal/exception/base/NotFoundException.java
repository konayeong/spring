package com.nhnacademy.springmvcfinal.exception.base;

public class NotFoundException extends RuntimeException {
    public NotFoundException(String message) {
        System.out.println("[NotFound] " + message);
    }
}
