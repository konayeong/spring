package com.nhnacademy.springmvcfinal.domain.user;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class User {
    private final String userId;
    private final String password;
    private final String name;
    private final Role role;

    public static User create(String userId, String password, String name, Role role) {
        return new User(userId, password, name, role);
    }

}