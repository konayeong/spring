package com.example.core.price.domain;

import com.opencsv.bean.CsvBindByName;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class Price {
    @CsvBindByName(column = "순번")
    private Long id;
    @CsvBindByName(column = "지자체명")
    private String city;
    @CsvBindByName(column = "업종")
    private String sector;
    @CsvBindByName(column = "단계")
    private Long step;
    @CsvBindByName(column = "구간시작(세제곱미터)")
    private Long intervalStart;
    @CsvBindByName(column = "구간끝(세제곱미터)")
    private Long intervalEnd;
    @CsvBindByName(column = "구간금액(원)")
    private Long intervalPrice;
//    @CsvBindByName(column = "단계별 기본요금(원)")
//    private Long basicPrice;

    public void setCity(String city) {
        this.city = city.trim();
    }

    public void setSector(String sector) {
        this.sector = sector.trim();
    }
}
