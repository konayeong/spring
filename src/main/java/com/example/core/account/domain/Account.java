package com.example.core.account.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor // OpenCSV가 객체 생성 시 기본 생성자 사용
@AllArgsConstructor
public class Account {
    @JsonProperty(value = "아이디")
    private Long id;
    @JsonProperty(value = "비밀번호")
    private String password;
    @JsonProperty(value = "이름")
    private String name;
}
