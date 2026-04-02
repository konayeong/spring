package com.example.core.common.exception.auth;

public class AuthenticationException extends RuntimeException {
    public AuthenticationException() {
        super("로그인 실패 : 비밀번호 불일치");
    }
}
