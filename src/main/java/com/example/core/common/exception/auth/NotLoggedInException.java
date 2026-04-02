package com.example.core.common.exception.auth;

public class NotLoggedInException extends RuntimeException {
    public NotLoggedInException() {
        super("현재 로그인된 사용자가 없습니다.");
    }
}