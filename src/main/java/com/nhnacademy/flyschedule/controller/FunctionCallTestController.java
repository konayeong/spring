package com.nhnacademy.flyschedule.controller;

import com.nhnacademy.flyschedule.service.FunctionCallTestService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/test/function-calling")
public class FunctionCallTestController {
    private final FunctionCallTestService functionCallTestService;

    /**
     * Function Calling 테스트
     * GET /api/test/function-calling?message=10과20의합은?
     * - 알맞는 tool을 찾아서 응답값을 가져온다.
     */
    @GetMapping
    public String testFunctionCalling(@RequestParam("message") String message) {
        long startTime = System.currentTimeMillis();
        String response = functionCallTestService.testFunctionCalling(message);
        long endTime = System.currentTimeMillis();
        String duration = (endTime - startTime) / 1000.0 + "초";

        return "[Function Calling 응답 (소요시간 : " + duration + ")]\n" + response;
    }
}
