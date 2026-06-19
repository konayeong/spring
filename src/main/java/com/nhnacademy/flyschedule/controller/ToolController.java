package com.nhnacademy.flyschedule.controller;

import com.nhnacademy.flyschedule.dto.AirlineGroup;
import com.nhnacademy.flyschedule.dto.ToolFlightsSearchRequest;
import com.nhnacademy.flyschedule.dto.api.AirlineInfoResponse;
import com.nhnacademy.flyschedule.dto.api.AirportInfoResponse;
import com.nhnacademy.flyschedule.tool.AirlineInfoTool;
import com.nhnacademy.flyschedule.tool.AirportInfoTool;
import com.nhnacademy.flyschedule.tool.FlightSearchTool;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;
/**
 * MCP Tool 요청 처리
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/mcp")
public class ToolController {
    private final FlightSearchTool flightSearchTool;
    private final AirportInfoTool airportInfoTool;
    private final AirlineInfoTool airlineInfoTool;

    @PostMapping("/flights")
    public List<AirlineGroup> getFlights(@RequestBody ToolFlightsSearchRequest request) {
        return flightSearchTool.searchFlightsByAirLine(
            request.depAirport(), request.arrAirport(), request.date(),
            request.afterTime(), request.beforeTime(), request.minPrice(), request.maxPrice()
        );
    }

    @GetMapping("/airports")
    public List<AirportInfoResponse> getAirports() {
        return airportInfoTool.getAllAirportInfo();
    }

    @GetMapping("/airport-code")
    public String getAirportCode(@RequestParam("name") String name) {
        return airportInfoTool.getAirportCodeByName(name);
    }

    @GetMapping("/airlines")
    public List<AirlineInfoResponse> getAirlines() {
        return airlineInfoTool.getAirlineInfo();
    }

    @GetMapping("/airline-id")
    public String getAirlineId(@RequestParam("name") String name) {
        return airlineInfoTool.getAirlineName(name);
    }
}
