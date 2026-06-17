package com.nhnacademy.flyschedule.tool;

import com.nhnacademy.flyschedule.agent.*;
import com.nhnacademy.flyschedule.dto.AirlineGroup;
import com.nhnacademy.flyschedule.dto.api.FlightInfoResponse;
import com.nhnacademy.flyschedule.mcp.ToolResultCapture;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;
import java.time.LocalTime;
import java.util.List;

/**
 * 항공편 조회를 위한 Tool
 * - AI와 연결되는 진입점
 * - Step6 : service 분리 없이 tool에서 모두 처리
 * @Tool : AI 모델이 호출할 수 있는 도구 정의
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class FlightSearchTool {

    private final DateParserAgent dateParserAgent;
    private final AirportCodeAgent airportCodeAgent;
    private final FlightSearchAgent flightSearchAgent;
    private final TimeFilterAgent timeFilterAgent;
    private final PriceFilterAgent priceFilterAgent;
    private final GroupingAgent groupingAgent;

    /**
     * 항공사별 항공편 조회
     */
    @Tool(description = """
            출발지, 도착지, 날짜, 시간(선택), 금액(선택)을 입력 받아 항공편을 조회 후 항공사별로 그룹지어 반환합니다.
            
            언제 사용
          - 내일 광주에서 제주 가는 항공편 알려줘
          - 내일 오후 2시 이후 광주에서 제주 가는 항공편 알려줘
          - 내일 광주에서 제주 가는 5만원 이하 항공편 알려줘
          - 내일 오후 2시 이후 광주에서 제주 가는 5만원 이하 항공편 알려줘
           
            
            파라미터
            - departure : 출발지
            - arrival : 도착지
            - date : 출발 날짜 ('오늘, 내일, 모레, N일 후' 형식을 지원)
            - afterTime : 이후 시간 (선택)
            - beforeTime : 이전 시간 (선택)
            - minPrice : 최소 가격 (선택)
            - maxPrice : 최대 가격 (선택)
            
            반환값
            - 공항, 날짜로 조회된 항공편을 항공사별로 그룹지어 항공사별 최대 3개의 항공편을 반환합니다.
            """)
    public List<AirlineGroup> searchFlightsByAirLine(
            @ToolParam(description = "출발지 (예: 광주, 광주공항, 광주 공항)") String departure,
            @ToolParam(description = "도착지 (예: 제주, 제주공항, 제주 공항)") String arrival,
            @ToolParam(description = "날짜 (예: 오늘, 내일, 모레, 10일 후)") String date,
            @ToolParam(description = "이후 시간 (선택)") String afterTime,
            @ToolParam(description = "이전 시간 (선택)") String beforeTime,
            @ToolParam(description = "최소 가격 (선택)") Integer minPrice,
            @ToolParam(description = "최대 가격 (선택)") Integer maxPrice) {
        log.info(" [항공편 검색 Tool 호출] 출발={} 도착={} 날짜={} 이후시간={} 이전시간={} 최소가격={} 최대가격={} ",
                departure, arrival, date, afterTime, beforeTime, minPrice, maxPrice);

        String parsedDate = dateParserAgent.parseDate(date);

        String depAirportId = airportCodeAgent.getAirportCode(departure);
        String arrAirportId = airportCodeAgent.getAirportCode(arrival);

        List<FlightInfoResponse> flights = flightSearchAgent.search(depAirportId, arrAirportId, parsedDate);

        log.info("검색 결과 {}건", flights.size());

        // 이후 시간 필터
        if (afterTime != null && !afterTime.isBlank()) {
            LocalTime filterTime = timeFilterAgent.parseTime(afterTime);
            flights = timeFilterAgent.filterAfterTime(flights, filterTime);
            log.info("이후 시간 필터 적용 후 {}건", flights.size());
        }

        // 이전 시간 필터
        if (beforeTime != null && !beforeTime.isBlank()) {
            LocalTime filterTime = timeFilterAgent.parseTime(beforeTime);
            flights = timeFilterAgent.filterBeforeTime(flights, filterTime);
            log.info("이전 시간 필터 적용 후 {}건", flights.size());
        }

        // 가격 필터
        if (minPrice != null || maxPrice != null) {
            flights = priceFilterAgent.filterByPriceRange(    flights, minPrice, maxPrice);
            log.info("가격 필터 적용 후 {}건", flights.size());
        }

        // 그룹핑
        List<AirlineGroup> result = groupingAgent.groupByAirline(flights);

        // 항공사별 최대 3개
        result = limitFlights(result);

        log.info("최종 결과 {}개 항공사", result.size());

        ToolResultCapture.capture("searchFlights", result);

        return result;
    }

    /**
     * 항공사별 최대 3개만 반환
     */
    private List<AirlineGroup> limitFlights(List<AirlineGroup> groups) {
        return groups.stream()
                .map(group ->
                        new AirlineGroup(group.airlineName(),
                                group.flights().size() > 3
                                        ? group.flights().subList(0, 3)
                                        : group.flights()
                        )
                )
                .toList();
    }
}
