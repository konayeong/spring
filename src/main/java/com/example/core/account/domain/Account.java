package com.example.core.account.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.opencsv.bean.CsvBindByName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor // OpenCSV가 객체 생성 시 기본 생성자 사용
@AllArgsConstructor
public class Account {
    @CsvBindByName(column = "아이디")
    @JsonProperty(value = "아이디")
    private Long id;
    @CsvBindByName(column = "비밀번호")
    @JsonProperty(value = "비밀번호")
    private String password;
    @CsvBindByName(column = "이름")
    @JsonProperty(value = "이름")
    private String name;

    public void setPassword(String password) {
        this.password = password.trim();
    }

    public void setName(String name) {
        this.name = name.trim();
    }
}
