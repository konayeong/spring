package com.nhnacademy.flyschedule.agent;

import com.nhnacademy.flyschedule.service.FlightApiClient;
import com.nhnacademy.flyschedule.dto.AirlineInfoResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class AirlineIdAgent {
    private final FlightApiClient flightApiClient;
    private Map<String, String> AIRLINE_ID_MAP = new HashMap<>();

    public String getAirlineId(String airlineName) {
        if(airlineName == null || airlineName.isBlank()) {
            throw new IllegalArgumentException("항공사 이름을 입력해주세요.");
        }

        if (AIRLINE_ID_MAP == null) {
            loadCache();
        }

        String normalized = airlineName
                .trim()
                .replace("항공", "")
                .replace(" ", "");

        if (normalized == null) {
            throw new IllegalArgumentException("알 수 없는 항공사입니다.");
        }

        String id = AIRLINE_ID_MAP.get(normalized);
        if(id == null) {
            log.warn("존재하지 않는 항공사 : {}", airlineName);
            throw new IllegalArgumentException("존재하지 않는 항공사입니다. " + airlineName);
        }

        return id;
    }

    private void loadCache() {
        List<AirlineInfoResponse> airlineList = flightApiClient.getAirlineList();
        AIRLINE_ID_MAP.clear();
        airlineList.forEach(airport -> AIRLINE_ID_MAP.put(
                airport.airlineName().replace("항공",""),
                airport.airlineId()
        ));
    }
}
