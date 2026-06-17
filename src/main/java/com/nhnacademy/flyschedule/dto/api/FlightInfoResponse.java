package com.nhnacademy.flyschedule.dto.api;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 공공데이터 API : 항공편 정보 응답
 */
public record FlightInfoResponse (
        @JsonProperty("vihicleId")
        String vihicleId,

        @JsonProperty("airlineNm")
        String airlineName,

        @JsonProperty("depPlandTime")
        String depPlandTime,

        @JsonProperty("arrPlandTime")
        String arrPlandTime,

        @JsonProperty("economyCharge")
        String economyCharge,

        @JsonProperty("prestigeCharge")
        String prestigeCharge,

        @JsonProperty("depAirportNm")
        String depAirportNm,

        @JsonProperty("arrAirportNm")
        String arrivalAirportName
){
}
