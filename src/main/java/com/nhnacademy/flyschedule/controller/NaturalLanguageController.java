package com.nhnacademy.flyschedule.controller;

import com.nhnacademy.flyschedule.dto.AiFlightSearchResult;
import com.nhnacademy.flyschedule.dto.FlightSearchRequest;
import com.nhnacademy.flyschedule.dto.LlmType;
import com.nhnacademy.flyschedule.service.FlightSearchCoordinator;
import com.nhnacademy.flyschedule.service.OrchestrationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 자연어 -> coordinator / orchestrator 방식으로 처리
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/flight/")
public class NaturalLanguageController {
    private final FlightSearchCoordinator flightSearchCoordinator;
    private final OrchestrationService orchestrationService;

    @PostMapping("/coordinator")
    public AiFlightSearchResult coordinator(@RequestBody FlightSearchRequest request) {
        String message = request.message();
        LlmType llmType = request.llmType();

        return flightSearchCoordinator.search(message, llmType);
    }

    @PostMapping("/orchestration")
    public AiFlightSearchResult orchestrator(@RequestBody FlightSearchRequest request) {
        String message = request.message();
        LlmType llmType = request.llmType();
        return orchestrationService.search(message, llmType);
    }
}
