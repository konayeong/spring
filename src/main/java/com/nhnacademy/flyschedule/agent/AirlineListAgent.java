package com.nhnacademy.flyschedule.agent;

import com.nhnacademy.flyschedule.service.FlightApiClient;
import com.nhnacademy.flyschedule.dto.api.AirlineInfoResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 항공사 목록 조회와 항공사 아이디 변환 담당
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AirlineListAgent {
    private final FlightApiClient flightApiClient;
    private final AirlineIdAgent airlineIdAgent;

    /**
     * 전체 항공사 목록
     */
    public List<AirlineInfoResponse> getAirlineList() {
        log.info("AirlineListAgent : 항공사 목록 조회");
        List<AirlineInfoResponse> airlines = flightApiClient.getAirlineList();
        log.info("항공사 {}개 조회 완료", airlines.size());
        return airlines;
    }

    /**
     * 항공사 이름 -> 항공사 아이디 조회
     */
    public String getAirlineId(String airlineName) {
        return airlineIdAgent.getAirlineId(airlineName);
    }

}
