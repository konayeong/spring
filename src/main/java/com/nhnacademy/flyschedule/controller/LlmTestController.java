package com.nhnacademy.flyschedule.controller;

import com.nhnacademy.flyschedule.dto.AiFlightSearchResult;
import com.nhnacademy.flyschedule.service.FlightSearchCoordinator;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class LlmTestController {
    private final FlightSearchCoordinator flightSearchCoordinator;

    @PostMapping("/coordinator")
    public AiFlightSearchResult test(@RequestBody Map<String, String> request) {
        String message = request.get("message");

        return flightSearchCoordinator.search(message);
    }
}
