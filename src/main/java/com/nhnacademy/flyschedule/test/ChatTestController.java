package com.nhnacademy.flyschedule.test;

import com.nhnacademy.flyschedule.service.SimpleChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * HTTP 요청을 받아 Service를 호출하고 결과를 반환
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/chat")
public class ChatTestController {
    private final SimpleChatService simpleChatService;

    /**
     * Ollama로 질문
     * GET /api/chat/ollama?question=안녕
     */
    @GetMapping("/ollama")
    public String askOllama(@RequestParam("question") String question) {
        long startTime = System.currentTimeMillis();

        String response = simpleChatService.askOllama(question);

        long endTime = System.currentTimeMillis();
        String duration = (endTime - startTime) / 1000.0 + "초";

        return "[Ollama 응답 (소요시간 : " + duration + ")]\n" + response;
    }

    /**
     * Gemini로 질문
     * GET /api/chat/gemini?question=안녕
     */
    @GetMapping("/gemini")
    public String askGemini(@RequestParam("question") String question) {
        long startTime = System.currentTimeMillis();

        String response = simpleChatService.askGemini(question);

        long endTime = System.currentTimeMillis();
        String duration = (endTime - startTime) / 1000.0 + "초";

        return "[Gemini 응답 (소요시간 : " + duration + ")]\n" + response;
    }
}
