package com.nhnacademy.springmvcfinal.exception;

import com.nhnacademy.springmvcfinal.exception.base.NotFoundException;

public class UserNotFoundException extends NotFoundException {
    public UserNotFoundException(String id) {
        super("ID : " + id + " 와 일치하는 고객이 존재하지 않습니다.");
    }
}
