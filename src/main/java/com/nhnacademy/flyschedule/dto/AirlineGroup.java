package com.nhnacademy.flyschedule.dto;

import java.util.List;

/**
 *  최종 결과 그룹
 */
public record AirlineGroup (
        String airlineName,
        List<FlightInfoResponse> flights
){
}
