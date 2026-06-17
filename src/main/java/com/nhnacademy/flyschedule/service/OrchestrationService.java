package com.nhnacademy.flyschedule.service;

import com.nhnacademy.flyschedule.dto.AiFlightSearchResult;
import com.nhnacademy.flyschedule.dto.LlmType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

/**
 * LLM이 Tool 호출을 스스로 판단하고, 개발자는 순서를 지정할 필요가 없음
 */
@Slf4j
@Service
public class OrchestrationService {
    private final ChatClient ollamaClient;
    private final ChatClient geminiClient;

    public OrchestrationService(
            @Qualifier("ollamaChatClientBuilder") ChatClient.Builder ollamaBuilder,
            @Qualifier("geminiChatClientBuilder") ChatClient.Builder geminiBuilder) {
        this.ollamaClient = ollamaBuilder.build();
        this.geminiClient = geminiBuilder.build();
    }

    /**
     * LLM이 Function Calling으로 자율적으로 MCP Tools르 선택/호출
     */
    public AiFlightSearchResult search(String message, LlmType llmType) {
        log.info("[Orchestrator] Start");

        // LLM 선택
        ChatClient chatClient = llmType.equals(LlmType.GEMINI) ? geminiClient : ollamaClient;

        String prompt = """
                    당신은 항공편 검색 시스템입니다.
                
                    반드시 Tool을 사용해서 데이터를 수집하세요.
                    사용자의 문장에서 반드시 아래 정보를 추출해야 합니다.
                    - depAirport(출발지)
                    - arrAirport(도착지)
                    - date
                    
                    아래 정보는 선택사항으로 존재하면 변환 규칙을 지켜 추출하고, 없으면 null을 넣으세요.
                      - afterTime / beforeTime은 반드시 사용자가 말한 시간의 "최소/최대 경계값"이다.
                      - "오전 11시 이후" → 11:00
                      - "오후 6시 이전" → 18:00
                      - 추가 보정 금지
                    
                    그리고 최종 결과는 반드시 아래 JSON 구조로 반환하세요:
                
                    {
                      "success": true,
                      "message": "",
                      "param": {...},
                      "data": [...]
                    }
                    
                    규칙:
                    - Tool 결과를 반드시 포함
                    - 설명 금지
                    - JSON 외 출력 금지
                    """;
        return chatClient.prompt()
                .system(prompt)
                .user(message)
                .call()
                .entity(AiFlightSearchResult.class);
    }
}

