package com.nhnacademy.flyschedule.controller;

import com.nhnacademy.flyschedule.agent.AirportListAgent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class WebController {
    private final AirportListAgent airportListAgent;

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/tool")
    public String tool(Model model) {
        model.addAttribute("airports", airportListAgent.getAirportList());
        return "mcp";
    }
}
