package com.nhnacademy.springsecurityfinal.exception;

public class MemberNotFoundException extends RuntimeException {
    public MemberNotFoundException(String id) {
        super("존재하지 않는 아이디입니다. " + id);
    }
}
