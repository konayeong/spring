package com.nhnacademy.flyschedule.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 항공사 정보 응답
 */
public record AirlineInfoResponse (
        @JsonProperty("airlineId")
        String airlineId,

        @JsonProperty("airlineNm")
        String airlineName
){
}
