package com.nhnacademy.flyschedule.service;

import com.nhnacademy.flyschedule.agent.*;
import com.nhnacademy.flyschedule.dto.*;
import com.nhnacademy.flyschedule.dto.api.FlightInfoResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * Coordinator : 순서 결정
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class FlightSearchCoordinator {
    private final LlmAnalysisService llmAnalysisService; // 자연어 -> 파라미터 추출
    private final DateParserAgent dateParserAgent;
    private final AirportCodeAgent airportCodeAgent;
    private final FlightSearchAgent flightSearchAgent;
    private final TimeFilterAgent timeFilterAgent;
    private final PriceFilterAgent priceFilterAgent;
    private final GroupingAgent groupingAgent;

    public AiFlightSearchResult  search(String message, LlmType llmType) {
        log.info("[Coordinator] Start");
        log.info("1단계 LLM 분석");
        FlightSearchParam param = llmAnalysisService.analyze(message, llmType);
        log.info("LLM 분석 결과 : {}", param);

        log.info("2단계 날짜 변환");
        String date = dateParserAgent.parseDate(param.date());

        log.info("3단계 공항 코드 변환");
        String depCode = airportCodeAgent.getAirportCode(param.departure());
        String arrCode = airportCodeAgent.getAirportCode(param.arrival());

        log.info("4단계 항공편 검색");
        List<FlightInfoResponse> flights = flightSearchAgent.search(depCode, arrCode, date);

        if(param.afterTime() != null) {
            flights = timeFilterAgent.filterAfterTime(flights, timeFilterAgent.parseTime(param.afterTime()));
        }

        if(param.minPrice() != null || param.maxPrice() != null) {
            flights = priceFilterAgent.filterByPriceRange(flights, param.minPrice(), param.maxPrice());
        }

        log.info("5단계 항공사 단위 그룹화");
        List<AirlineGroup> groups = groupingAgent.groupByAirline(flights);

        return AiFlightSearchResult.success(param, groups);
    }
}
