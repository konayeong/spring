package com.nhnacademy.flyschedule.tool;

import com.nhnacademy.flyschedule.agent.FlightSearchAgent;
import com.nhnacademy.flyschedule.dto.FlightInfoResponse;
import com.nhnacademy.flyschedule.mcp.ToolResultCapture;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 항공편 조회를 위한 Tool
 * - AI와 연결되는 진입점
 * - Step6 : service 분리 없이 tool에서 모두 처리
 * @Tool : AI 모델이 호출할 수 있는 도구 정의
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class FlightSearchTool implements AiTool{

    private final FlightSearchAgent flightSearchAgent;

    // TODO-R 반환값 response dto로 처리 ?
    /**
     * 항공사별 항공편 조회
     */
    @Tool(description = """
            출발 공항, 도착 공항, 날짜를 입력 받아 항공편을 조회 후 항공사별로 그룹지어 반환합니다.
            
            언제 사용
            - 광주공항에서 제주공항으로 내일 갈 수 있는 항공편을 알려줘.
            - 광주에서 제주로 10일 후에 갈 수 있는 항공편을 알려줘.
            
            파라미터
            - depAirport : 출발 공항 이름
            - arrAirport : 도착 공항 이름
            - date : 출발 날짜 ('오늘, 내일, 모레, N일 후' 형식을 지원)
            
            반환값
            - 공항, 날짜로 조회된 항공편을 항공사별로 그룹지어 항공사별 최대 3개의 항공편을 반환합니다.
            """)
    public Map<String, List<FlightInfoResponse>> searchFlightsByAirLine(
            @ToolParam(description = "출발 공항 이름 (예: 광주, 광주공항, 광주 공항)") String depAirport,
            @ToolParam(description = "도착 공항 이름 (예: 제주, 제주공항, 제주 공항)") String arrAirport,
            @ToolParam(description = "날짜 (예: 오늘, 내일, 모레, 10일 후)") String date) {
        log.info("[항공편 조회 Tool 호출] 출발 : {}, 도착 : {}, 날짜 : {}", depAirport, arrAirport, date);

        // A2A : Coordinator
        Map<String, List<FlightInfoResponse>> allFlights = flightSearchAgent.searchAndGroupByAirline(depAirport, arrAirport, date);

        Map<String, List<FlightInfoResponse>> limitFlights = limitedFlights(allFlights);

        log.info("[항공편 조회 Tool 응답] {}개 항공사, {}편", limitFlights.size(), limitFlights.values().stream().mapToInt(List::size).sum());

        // 결과 캡처
        ToolResultCapture.capture("searchFlightsByAirline", limitFlights);
        return limitFlights;
    }

    /**
     * 항공사별 항공편 조회 - 특정 시간 이후
     */
    @Tool(
            description = """
                    출발 공항, 도착 공항, 날짜, 출발 시간을 입력 받아 항공편을 조회 후 항공사별로 그룹지어 반환합니다.
            
                    언제 사용
                    - 광주공항에서 제주공항으로 내일 오후 2시 이후에 출발하는 항공편을 알려줘.
                    - 광주에서 제주로 10일 후 14:00 이후에 출발하는 항공편을 알려줘.
                    
                    파라미터
                    - depAirport : 출발 공항 이름
                    - arrAirport : 도착 공항 이름
                    - date : 출발 날짜 ('오늘, 내일, 모레, N일 후' 형식을 지원)
                    - afterTime : 출발 시간 ('14:00, 오전9시, 오후2시' 형식을 지원)
                    
                    반환값
                    - 공항, 날짜, 시간으로 조회된 항공편을 항공사별로 그룹지어 항공사별 최대 3개의 항공편을 반환합니다.
                    """
    )
    public Map<String, List<FlightInfoResponse>> searchFlightsWithTime(
            @ToolParam(description = "출발 공항 이름 (예: 광주, 광주공항, 광주 공항)") String depAirport,
            @ToolParam(description = "도착 공항 이름 (예: 제주, 제주공항, 제주 공항)") String arrAirport,
            @ToolParam(description = "날짜 (예: 오늘, 내일, 모레, 10일 후)") String date,
            @ToolParam(description = "기준 시간 (예: 14:00, 오후 2시)") String afterTime
    ) {
        log.info("[항공편 조회 with Time Tool 호출] 출발 : {}, 도착 : {}, 날짜 : {}", depAirport, arrAirport, date);

        Map<String, List<FlightInfoResponse>> allFlights = flightSearchAgent.searchWithTimeFilter(depAirport, arrAirport, date, afterTime);

        Map<String, List<FlightInfoResponse>> limitFlights = limitedFlights(allFlights);

        log.info("[항공편 조회 with Time Tool 응답] {}개 항공사, {}편", limitFlights.size(), limitFlights.values().stream().mapToInt(List::size).sum());

        ToolResultCapture.capture("searchFlightsAfterTime", limitFlights);
        return limitFlights;
    }

    /**
     * 항공사별 항공편 조회 - 특정 가격 범위 내
     */
    @Tool(
            description = """
                    출발 공항, 도착 공항, 날짜, 최소 금액, 최대 금액을 입력 받아 항공편을 조회 후 항공사별로 그룹지어 반환합니다.
            
                    언제 사용
                    - 광주공항에서 제주공항으로 내일 출발하는 30000만원 이상인 항공편을 알려줘
                    - 광주에서 제주로 10일 후 14:00 이후에 출발하는 20000원 이상, 50000원 이하인 항공편을 알려줘
                    
                    파라미터
                    - depAirport : 출발 공항 이름
                    - arrAirport : 도착 공항 이름
                    - date : 출발 날짜 ('오늘, 내일, 모레, N일 후' 형식을 지원)
                    - minPrice : 최소 금액 (선택 사항)
                    - maxPrice : 최대 금액 (선택 사항)
                    
                    반환값
                    - 공항, 날짜, 예산으로 조회된 항공편을 항공사별로 그룹지어 항공사별 최대 3개의 항공편을 반환합니다.
                    """
    )
    public Map<String, List<FlightInfoResponse>> searchFlightsWithPrice(
            @ToolParam(description = "출발 공항 이름 (예: 광주, 광주공항, 광주 공항)") String depAirport,
            @ToolParam(description = "도착 공항 이름 (예: 제주, 제주공항, 제주 공항)") String arrAirport,
            @ToolParam(description = "날짜 (예: 오늘, 내일, 모레, 10일 후)") String date,
            @ToolParam(description = "최소 가격 (선택 사항, 예: 30000)") Integer minPrice,
            @ToolParam(description = "최대 가격 (선택 사항, 예: 50000)") Integer maxPrice
    ) {
        log.info("[항공편 조회 with Price Tool 호출] 출발 : {}, 도착 : {}, 날짜 : {}", depAirport, arrAirport, date);

        Map<String, List<FlightInfoResponse>> allFlights = flightSearchAgent.searchWithPriceFilter(depAirport, arrAirport, date, minPrice, maxPrice);

        Map<String, List<FlightInfoResponse>> limitFlights = limitedFlights(allFlights);

        log.info("[항공편 조회 with Price Tool 응답] {}개 항공사, {}편", limitFlights.size(), limitFlights.values().stream().mapToInt(List::size).sum());

        ToolResultCapture.capture("searchFlightsWithPrice", limitFlights);
        return limitFlights;
    }

    /**
     * 항공사별 최대 3개
     */
    private Map<String, List<FlightInfoResponse>> limitedFlights(Map<String, List<FlightInfoResponse>> allFlights) {
        Map<String, List<FlightInfoResponse>> limitedFlights = new HashMap<>();

        allFlights.forEach((airline, flights) -> {
            if(flights.size() > 3) {
                limitedFlights.put(airline, flights.subList(0, 3));
            } else {
                limitedFlights.put(airline, flights);
            }
        });

        return limitedFlights;
    }
}
