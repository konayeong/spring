package com.example.core.common.exception.auth;

public class AccountNotFoundException extends RuntimeException {
    public AccountNotFoundException() {
        super("로그인 실패 : 회원이 존재하지 않습니다.");
    }
}
