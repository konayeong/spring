package com.nhnacademy.flyschedule.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 공항 정보 응답
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
