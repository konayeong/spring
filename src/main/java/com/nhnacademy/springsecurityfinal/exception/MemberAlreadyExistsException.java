package com.nhnacademy.springsecurityfinal.exception;

public class MemberAlreadyExistsException extends RuntimeException {
    public MemberAlreadyExistsException(String id) {
        super("이미 존재하는 아이디입니다. " + id);
    }
}
