package com.nhnacademy.flyschedule.dto.api;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 공공데이터 API : 항공사 정보 응답
 */
public record AirlineInfoResponse (
        @JsonProperty("airlineId")
        String airlineId,

        @JsonProperty("airlineNm")
        String airlineName
){
}
