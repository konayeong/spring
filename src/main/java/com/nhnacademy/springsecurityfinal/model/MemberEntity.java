package com.nhnacademy.springsecurityfinal.model;

import com.nhnacademy.springsecurityfinal.model.dto.MemberCreateRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

// DB 저장용
@Getter // Serialize
@NoArgsConstructor
@AllArgsConstructor
public class MemberEntity {
    private String id;
    private String name;
    private String password;
    private Integer age;
    private Role role;

    public MemberEntity(MemberCreateRequest request, String encodePassword) {
        this.id = request.getId();
        this.name = request.getName();
        this.password = encodePassword;
        this.age = request.getAge();
        this.role = request.getRole();
    }
}
