package com.nhnacademy.springmvcfinal.domain.user;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Role {
    USER(0, "고객"),
    ADMIN(1, "관리자");

    private final int value;
    private final String name;

    public static Role from(int value) {
        for(Role role : values()) {
            if(role.value == value) {
                return role;
            }
        }
        throw new IllegalArgumentException("Unknown value: " + value);
    }
}
