package com.nhnacademy.flyschedule.dto;

import java.util.List;

/**
 * 자연어 처리 결과 저장하는 DTO
 */
public record AiFlightSearchResult(
        boolean success,
        String message,
        FlightSearchParam param,
        List<AirlineGroup> data
) {
    public static AiFlightSearchResult success(FlightSearchParam param, List<AirlineGroup> data) {
        return new AiFlightSearchResult(true, "성공", param, data);
    }

    public static AiFlightSearchResult error(String message) {
        return new AiFlightSearchResult(false, message, null, List.of());
    }
}
