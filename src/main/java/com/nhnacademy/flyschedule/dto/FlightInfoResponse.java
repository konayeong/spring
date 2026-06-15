package com.nhnacademy.flyschedule.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 항공편 정보 응답
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

        // TODO-Q Integer로 이렇게 바꿔도 되나?
        @JsonProperty("economyCharge")
        Integer economyCharge,

        @JsonProperty("prestigeCharge")
        String prestigeCharge,

        @JsonProperty("depAirportNm")
        String depAirportNm,

        @JsonProperty("arrAirportNm")
        String arrivalAirportName
){
}
