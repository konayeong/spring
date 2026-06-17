package com.nhnacademy.flyschedule.test;

import com.nhnacademy.flyschedule.agent.AirportCodeAgent;
import com.nhnacademy.flyschedule.agent.DateParserAgent;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/agent")
public class AgentTestController {
    private final DateParserAgent dateParserAgent;
    private final AirportCodeAgent airportCodeAgent;

    /**
     * 날짜 파싱 테스트
     */
    @GetMapping("/date-parse")
    public String testDateParser(@RequestParam("input") String date) {
        try {
            String result = dateParserAgent.parseDate(date);
            return "날짜 파싱 결과: " + date + " -> " + result;
        } catch (Exception e) {
            return "파싱 실패: " + e.getMessage();
        }
    }

    /**
     * 공항 코드 변환 테스트
     */
    @GetMapping("/airport-code")
    public String testAirportCode(@RequestParam("input") String airportName) {
        try {
            String result = airportCodeAgent.getAirportCode(airportName);
            return "공항 코드 변환: " + airportName + " -> " + result;
        } catch (Exception e) {
            return "변환 실패: " + e.getMessage();
        }
    }

    /**
     * 에이전트 체이닝 테스트
     */
    @GetMapping("/chain")
    public String testAgentChaining(@RequestParam("departure") String depAirport,
                                    @RequestParam("arrival") String arrAirport,
                                    @RequestParam("date") String date) {
        try {
            String depCode = airportCodeAgent.getAirportCode(depAirport);
            String arrCode = airportCodeAgent.getAirportCode(arrAirport);
            String formattedDate = dateParserAgent.parseDate(date);

            return String.format("에이전트 체이닝 결과 : " +
                    "출발 : %s -> %s " +
                    "도착 : %s -> %s " +
                    "날짜 : %s -> %s", depAirport, depCode, arrAirport, arrCode, date, formattedDate);
        } catch (Exception e) {
            return "처리 실패: " + e.getMessage();
        }
    }
}
