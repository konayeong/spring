package com.nhnacademy.flyschedule.agent;

import com.nhnacademy.flyschedule.service.FlightApiClient;
import com.nhnacademy.flyschedule.dto.FlightInfoResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 항공편 검색 A2A 에이전트
 * [코디네이터 에이전트]
 * - 하위 에이전트들을 조율하여 작업을 수행하는 에이전트
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class FlightSearchAgent {

    private final FlightApiClient flightApiClient;
    private final DateParserAgent dateParserAgent;
    private final AirportCodeAgent airportCodeAgent;
    private final TimeFilterAgent timeFilterAgent;
    private final PriceFilterAgent priceFilterAgent;
    private final GroupingAgent groupingAgent;

    public Map<String, List<FlightInfoResponse>> searchAndGroupByAirline(String departure, String arrival, String date) {
        String parsedDate = dateParserAgent.parseDate(date);

        String depAirportId = airportCodeAgent.getAirportCode(departure);
        String arrAirportId = airportCodeAgent.getAirportCode(arrival);

        List<FlightInfoResponse> flights = flightApiClient.getFlightSchedule(depAirportId, arrAirportId, parsedDate);

        return groupingAgent.groupByAirline(flights);
    }

    public Map<String, List<FlightInfoResponse>> searchWithTimeFilter(String departure, String arrival, String date, String afterTime) {
        Map<String, List<FlightInfoResponse>> grouped = searchAndGroupByAirline(departure, arrival, date);

        LocalTime filterTime = timeFilterAgent.parseTime(afterTime);

        return grouped.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> timeFilterAgent.filterAfterTime(entry.getValue(), filterTime)
                ));
    }

    public Map<String, List<FlightInfoResponse>> searchWithPriceFilter(String departure, String arrival, String date, Integer minPrice, Integer maxPrice) {
        Map<String, List<FlightInfoResponse>> grouped = searchAndGroupByAirline(departure, arrival, date);

        return grouped.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> priceFilterAgent.filterByPriceRange(
                                entry.getValue(), minPrice, maxPrice)
                ));
    }
}