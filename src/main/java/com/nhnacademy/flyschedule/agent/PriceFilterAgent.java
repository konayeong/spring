package com.nhnacademy.flyschedule.agent;

import com.nhnacademy.flyschedule.dto.FlightInfoResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 운임 기준 항공편 필터링
 */
@Slf4j
@Service
public class PriceFilterAgent {

    public List<FlightInfoResponse> filterByPriceRange(List<FlightInfoResponse> flights, Integer minPrice, Integer maxPrice) {
        log.info("[PriceFilterAgent] 가격 범위 내의 항공편 필터링 시작");
        if (flights == null || flights.isEmpty()) {
            log.warn("항공편이 비어있습니다.");
            return List.of();
        }

        return flights.stream()
                .filter(flight -> {
                    Integer price = flight.economyCharge();
                    if (price == null || price == 0) return false;
                    if (minPrice != null && price < minPrice) return false;
                    return maxPrice == null || price <= maxPrice;
                })
                .collect(Collectors.toList());
    }
}