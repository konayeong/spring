package com.nhnacademy.flyschedule.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClientRequest;
import org.springframework.ai.chat.client.ChatClientResponse;
import org.springframework.ai.chat.client.advisor.api.CallAdvisor;
import org.springframework.ai.chat.client.advisor.api.CallAdvisorChain;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * 사용자 정의 로깅 Advisor
 */
@Slf4j
@Component
public class ChatLoggingAdvisor implements CallAdvisor {

    private final Map<String, Long> startTimeMap = new HashMap<>(); // 요청 시작 시간

    @Override
    public ChatClientResponse adviseCall(ChatClientRequest chatClientRequest, CallAdvisorChain callAdvisorChain) {
        String requestId = UUID.randomUUID().toString().substring(0, 8); // 고유한 요청 ID
        startTimeMap.put(requestId, System.currentTimeMillis());

        log.info("=========ChatLoggingAdvisor=========");
        log.info("요청 ID : {}", requestId);
        log.info("사용자 요청 : {}", chatClientRequest.prompt().getUserMessage().getText());
        log.info("===================================");

        try {
            ChatClientResponse chatClientResponse = callAdvisorChain.nextCall(chatClientRequest);
            long duration = System.currentTimeMillis() - startTimeMap.get(requestId); // 소요 시간

            log.info("=========ChatLoggingAdvisor=========");
            log.info("응답 ID : {}" ,requestId);
            log.info("실행 시간 : {}", duration);
            log.info("응답 내용 : {}", chatClientResponse.chatResponse().getResult().getOutput().getText()); // LLM 답변
            log.info("===================================");
            return chatClientResponse;
        } finally {
            startTimeMap.remove(requestId);
        }
    }

    @Override
    public String getName() {
        return "ChatLoggingAdvisor"; // 로그 식별용
    }

    @Override
    public int getOrder() { // 우선순위
        return 0;
    }
}
