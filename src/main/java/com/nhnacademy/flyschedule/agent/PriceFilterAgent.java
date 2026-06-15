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
        if (flights == null || flights.isEmpty()) {
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

    public FlightInfoResponse findCheapest(List<FlightInfoResponse> flights) {
        return flights.stream()
                .filter(f -> f.economyCharge() != null && f.economyCharge() > 0)
                .min((f1, f2) -> f1.economyCharge().compareTo(f2.economyCharge()))
                .orElse(null);
    }

    public double calculateAveragePrice(List<FlightInfoResponse> flights) {
        return flights.stream()
                .filter(f -> f.economyCharge() != null && f.economyCharge() > 0)
                .mapToInt(FlightInfoResponse::economyCharge)
                .average()
                .orElse(0.0);
    }
}