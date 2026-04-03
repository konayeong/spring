package com.example.core.price.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Price {
    @JsonProperty(value = "순번")
    private Long id;
    @JsonProperty(value = "지자체명")
    private String city;
    @JsonProperty(value = "업종")
    private String sector;
    @JsonProperty(value = "단계")
    private Long step;
    @JsonProperty(value = "구간시작(세제곱미터)")
    private Long intervalStart;
    @JsonProperty(value = "구간끝(세제곱미터)")
    private Long intervalEnd;
    @JsonProperty(value = "구간금액(원)")
    private Long intervalPrice;
    @JsonProperty(value = "단계별 기본요금(원)")
    private Long basicPrice;
}
