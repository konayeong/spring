package com.nhnacademy.springsecurityfinal.model;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum Role {
    ADMIN, MEMBER, GOOGLE;

    // 역직렬화
    @JsonCreator
    public static Role fromString(String roleStr) {
        for(Role role : Role.values()) {
            if(role.name().equalsIgnoreCase(roleStr)) {
                return role;
            }
        }
        return MEMBER; // default
    }

    @Override
    public String toString() {
        return this.name().toLowerCase(); // 소문자 응답
    }
}
