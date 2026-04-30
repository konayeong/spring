package com.nhnacademy.springsecurityfinal.model.dto;

import com.nhnacademy.springsecurityfinal.model.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MemberCreateRequest {
    private String id;
    private String name;
    private String password;
    private Integer age;
    private Role role;
}
