package com.nhnacademy.flyschedule.dto.api;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 공공데이터 API : 공항 정보 응답
 */
public record AirportInfoResponse (
        @JsonProperty("airportId")
        String airportId,

        @JsonProperty("airportNm")
        String airportName,

        @JsonProperty("region")
        String region
) {
}
