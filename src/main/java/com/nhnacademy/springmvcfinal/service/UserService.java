package com.nhnacademy.springmvcfinal.service;

import com.nhnacademy.springmvcfinal.domain.user.User;

public interface UserService {
    User doLogin(String id, String password);
}
