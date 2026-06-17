package com.nhnacademy.flyschedule.service;

import com.nhnacademy.flyschedule.dto.FlightSearchParam;
import com.nhnacademy.flyschedule.dto.FlightSearchRequest;
import com.nhnacademy.flyschedule.dto.LlmType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

/**
 * LLM을 활용한 자연어 분석 서비스
 * 사용자의 자연어 메시지에서 항공편 검색 파라미터를 추출합니다.
 */
@Slf4j
@Service
public class LlmAnalysisService {

    private final ChatClient geminiClient;
    private final ChatClient ollamaClient;

    public LlmAnalysisService(@Qualifier("geminiChatClientBuilder") ChatClient.Builder geminiBuilder,
                              @Qualifier("ollamaChatClientBuilder") ChatClient.Builder ollamaBuilder) {
        this.geminiClient = geminiBuilder.build();
        this.ollamaClient = ollamaBuilder.build();
    }

    public FlightSearchParam analyze(String message, LlmType llmType) {
        log.info("LLM 분석 시작 {}", message);

        ChatClient chatClient = switch (llmType) {
            case GEMINI -> geminiClient;
            case OLLAMA -> ollamaClient;
        };

        return chatClient.prompt()
                .system("""
                        당신은 항공편 검색 파라미터 추출기입니다.
                        사용자의 메시지에서 아래 정보를 추출하세요.
                        - 출발지, 도착지, 날짜, 이후 시간, 이전 시간, 최소 가격, 최대 가격
                        
                        규칙
                        - 날짜는 사용자가 말한 표현을 유지합니다.
                        - 예 : 내일, 모레 , 10일 후
                       
                        - 이후 시간, 이전 시간은 HH:mm형식으로 추출합니다.
                        - 가격은 숫자만 추출합니다. : 10만원 이하 -> maxPrice=100000
                        
                        찾을 수 없는 값은 null로 설정합니다.
                        """)
                .user(message)
                .call()
                .entity(FlightSearchParam.class);
    }
}
