package com.nhnacademy.flyschedule.agent;

import com.nhnacademy.flyschedule.dto.FlightInfoResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

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

        Map<String, List<FlightInfoResponse>> grouped = flights.stream()
                .collect(Collectors.groupingBy(
                        flight -> {
                            String airlineName = flight.airlineName();
                            if (airlineName == null || airlineName.trim().isEmpty()) {
                                String vihicleId = flight.vihicleId();
                                if (vihicleId != null && vihicleId.length() >= 2) {
                                    return vihicleId.substring(0, 2);
                                }
                                return "알 수 없는 항공사";
                            }
                            return airlineName;
                        },
                        TreeMap::new,
                        Collectors.toList()
                ));

        log.info("그룹핑 완료: {}개 항공사, {}개 항공편", grouped.size(), flights.size());
        return grouped;
    }
}
