package com.nhnacademy.flyschedule.agent;

import com.nhnacademy.flyschedule.service.FlightApiClient;
import com.nhnacademy.flyschedule.dto.AirportInfoResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 공항 정보 조회와 공항 코드 변환 담당
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AirportListAgent {
    private final FlightApiClient flightApiClient;
    private final AirportCodeAgent airportCodeAgent;

    /**
     * 전체 공항 목록
     */
    public List<AirportInfoResponse> getAirportList() {
        log.info("AirportListAgent : 공항 목록 조회");
        List<AirportInfoResponse> airports = flightApiClient.getAirportList();
        log.info("공항 {}개 조회 완료", airports.size());
        return airports;
    }

    /**
     * 공항 이름 -> 공항 코드 조회
     */
    public String getAirportCode(String airportName) {
       return airportCodeAgent.getAirportCode(airportName);
    }
}