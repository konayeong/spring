package com.nhnacademy.flyschedule.agent;

import com.nhnacademy.flyschedule.service.FlightApiClient;
import com.nhnacademy.flyschedule.dto.AirportInfoResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 공항 이름 -> 공항 코드
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AirportCodeAgent {
    private final FlightApiClient flightApiClient;
    private Map<String, String> AIRPORT_CODE_MAP = new HashMap<>();
    // TODO 예제 - static 블록으로 매핑 테이블 초기화 -> 어떤 방법이 더 괜찮은건지 ?
    public String getAirportCode(String airportName) {
        if(airportName == null || airportName.isBlank()) {
            throw new IllegalArgumentException("공항 이름을 입력해주세요.");
        }

        if(AIRPORT_CODE_MAP.isEmpty()) {
            loadCache();
        }

        String normalized = airportName
                .trim()
                .replace("공항", "")
                .replace(" ", "");

        // 공항 코드를 입력하면 그대로 반환
        if(normalized.matches("NAARK[A-Z]{2}")) {
            return normalized;
        }

        String code = AIRPORT_CODE_MAP.get(normalized);
        if(code == null) {
            log.warn("존재하지 않는 공항 : {}", airportName);
            throw new IllegalArgumentException("존재하지 않는 공항입니다: " + airportName);
        }

        return code;
    }

    private void loadCache() {
        List<AirportInfoResponse> airportList = flightApiClient.getAirportList();
        AIRPORT_CODE_MAP.clear();
        airportList.forEach(airport -> AIRPORT_CODE_MAP.put(
                airport.airportName(),
                airport.airportId()
        ));
    }
}
