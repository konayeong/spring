package com.nhnacademy.flyschedule.agent;

import com.nhnacademy.flyschedule.service.FlightApiClient;
import com.nhnacademy.flyschedule.dto.api.FlightInfoResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.*;

// TODO-R 카페인 캐시, 코드 전체 다시 확인 필요
@Slf4j
@Service
@RequiredArgsConstructor
public class FlightSearchAgent {
    private final FlightApiClient flightApiClient;

    /**
     * 기본 검색
     * @param departureId : 출발 공항
     * @param arrivalId : 도착 공항
     * @param date : 출발일
     */
    public List<FlightInfoResponse> search(String departureId, String arrivalId, String date) {
        log.info("[FlightSearchAgent] 항공편 검색 시작");
        return flightApiClient.getFlightSchedule(departureId, arrivalId, date);
    }
}
