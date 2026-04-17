package com.nhnacademy.springmvcfinal.exception;

import com.nhnacademy.springmvcfinal.exception.base.BadRequestException;

public class LoginFailedException extends BadRequestException {
    public LoginFailedException() {
        super("로그인 실패, 아이디 또는 비밀번호를 확인해주세요");
    }
}
