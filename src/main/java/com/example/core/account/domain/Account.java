package com.example.core.account.domain;

import com.opencsv.bean.CsvBindByName;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor // OpenCSV가 객체 생성 시 기본 생성자 사용
public class Account {
    @CsvBindByName(column = "아이디")
    private Long id;
    @CsvBindByName(column = "비밀번호")
    private String password;
    @CsvBindByName(column = "이름")
    private String name;

    // TODO-Q 파싱할 때 공백 없애고 가져오는 방법 (openCSV)
    public void setPassword(String password) {
        this.password = password.trim();
    }

    public void setName(String name) {
        this.name = name.trim();
    }
}
