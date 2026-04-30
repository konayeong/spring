package com.nhnacademy.springsecurityfinal.service;

// 로그인 성공 실패
public interface LoginService {
    void fail(String id);
    void login(String id);
    boolean isBlocked(String id);
}
