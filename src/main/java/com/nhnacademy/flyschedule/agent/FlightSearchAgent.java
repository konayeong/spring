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
    // TODO-R 카페인 캐시 적용해서 캐싱 (파라미터 마다 다르게 해야함)
    private final FlightApiClient flightApiClient;
    private final DateParserAgent dateParserAgent;
    private final AirportCodeAgent airportCodeAgent;
    private final TimeFilterAgent timeFilterAgent;
    private final PriceFilterAgent priceFilterAgent;
    private final GroupingAgent groupingAgent;

    /**
     * 기본 검색
     * @param departure : 출발 공항
     * @param arrival : 도착 공항
     * @param date : 출발일
     */
    public Map<String, List<FlightInfoResponse>> searchAndGroupByAirline(String departure, String arrival, String date) {
        log.info("[FlightSearchAgent] 항공편 검색 시작");
        String parsedDate = dateParserAgent.parseDate(date);
        log.info("[FlightSearchAgent] {} -> {} 파싱", date, parsedDate);

        String depAirportId = airportCodeAgent.getAirportCode(departure);
        String arrAirportId = airportCodeAgent.getAirportCode(arrival);
        log.info("[FlightSearchAgent] 공항 이름 기반 코드 변환");

        List<FlightInfoResponse> flights = flightApiClient.getFlightSchedule(depAirportId, arrAirportId, parsedDate);

        return groupingAgent.groupByAirline(flights);
    }

    /**
     * 시간 필터를 적용하여 항공편 검색
     * @param departure : 출발 공항
     * @param arrival : 도착 공항
     * @param date : 출발일
     * @param afterTime : 검색 기준 시간
     */
    public Map<String, List<FlightInfoResponse>> searchWithTimeFilter(String departure, String arrival, String date, String afterTime) {
        log.info("[FlightSearchAgent] 시간 필터링 검색 시작");
        Map<String, List<FlightInfoResponse>> grouped = searchAndGroupByAirline(departure, arrival, date); // 기본 검색

        LocalTime filterTime = timeFilterAgent.parseTime(afterTime);
        log.info("[FlightSearchAgent] {} 이후 출발 항공편 필터링", filterTime);

        // 항공사별로 시간 필터링 적용
        return grouped.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, entry -> timeFilterAgent.filterAfterTime(entry.getValue(), filterTime)));
    }

    /**
     * 가격 필터를 적용하여 항공편 검색
     * @param departure : 출발 공항
     * @param arrival : 도착 공항
     * @param date : 출발일
     * @param minPrice : 최대 금액
     * @param maxPrice : 최소 금액
     */
    public Map<String, List<FlightInfoResponse>> searchWithPriceFilter(String departure, String arrival, String date, Integer minPrice, Integer maxPrice) {
        log.info("[FlightSearchAgent] 가격 필터링 검색 시작");
        Map<String, List<FlightInfoResponse>> grouped = searchAndGroupByAirline(departure, arrival, date); // 기본 검색

        return grouped.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, entry -> priceFilterAgent.filterByPriceRange(entry.getValue(), minPrice, maxPrice)));
    }
}