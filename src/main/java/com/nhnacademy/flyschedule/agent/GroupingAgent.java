package com.nhnacademy.flyschedule.agent;

import com.nhnacademy.flyschedule.dto.FlightInfoResponse;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * 항공편을 항공사별로 그룹핑
 */
@Slf4j
@Service
public class GroupingAgent {
    public Map<String, List<FlightInfoResponse>> groupByAirline(List<FlightInfoResponse> flights) {
        if (flights == null || flights.isEmpty()) {
            return new TreeMap<>();
        }

        Map<String, List<FlightInfoResponse>> grouped = new TreeMap<>();

        for (FlightInfoResponse flight : flights) {
            String airlineKey = getAirlineKey(flight);

            if (!grouped.containsKey(airlineKey)) {
                grouped.put(airlineKey, new ArrayList<>());
            }

            grouped.get(airlineKey).add(flight);
        }
        
        log.info("그룹핑 완료: {}개 항공사, {}개 항공편", grouped.size(), flights.size());
        return grouped;
    }

    @NotNull // 실제로 Null을 막는 기능 x, 개발자와 IDE에게 계약을 알려주는 표시
    private static String getAirlineKey(FlightInfoResponse flight) {
        String airlineKey;
        String airlineName = flight.airlineName();

        if (airlineName == null || airlineName.trim().isEmpty()) {
            String vihicleId = flight.vihicleId();

            if (vihicleId != null && vihicleId.length() >= 2) {
                airlineKey = vihicleId.substring(0, 2);
            } else {
                airlineKey = "알 수 없는 항공사";
            }
        } else {
            airlineKey = airlineName;
        }
        return airlineKey;
    }
}
