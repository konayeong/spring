package com.nhnacademy.springsecurityfinal.model.dto;

import com.nhnacademy.springsecurityfinal.model.MemberEntity;
import com.nhnacademy.springsecurityfinal.model.Role;
import lombok.Getter;

@Getter
public class MemberResponse {
    private String id;
    private String name;
    private Integer age;
    private Role role;

    public MemberResponse(MemberEntity memberEntity) {
        this.id = memberEntity.getId();
        this.name = memberEntity.getName();
        this.age = memberEntity.getAge();
        this.role = memberEntity.getRole();
    }
}
