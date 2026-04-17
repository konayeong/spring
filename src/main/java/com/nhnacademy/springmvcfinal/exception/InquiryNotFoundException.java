package com.nhnacademy.springmvcfinal.exception;

import com.nhnacademy.springmvcfinal.exception.base.NotFoundException;

public class InquiryNotFoundException extends NotFoundException {
    public InquiryNotFoundException(int id) {
        super("ID : " + id + " 와 일치하는 문의글이 존재하지 않습니다.");
    }
}
