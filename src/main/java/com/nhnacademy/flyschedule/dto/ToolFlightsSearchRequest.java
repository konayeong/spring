package com.nhnacademy.flyschedule.dto;

public record ToolFlightsSearchRequest (
        String depAirport,
        String arrAirport,
        String date,
        String afterTime,
        String beforeTime,
        Integer minPrice,
        Integer maxPrice
){
}
