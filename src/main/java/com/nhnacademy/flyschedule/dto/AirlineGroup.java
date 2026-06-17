package com.nhnacademy.flyschedule.dto;

import com.nhnacademy.flyschedule.dto.api.FlightInfoResponse;

import java.util.List;

/**
 *  항공편 조회 : 항공사별 그룹화
 */
public record AirlineGroup (
        String airlineName,
        List<FlightInfoResponse> flights
){
}
