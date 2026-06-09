package com.nhnacademy.flyschedule.service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class SimpleChatService {
    private final ChatClient ollamaChatClient;
    private final ChatClient geminiChatClient;

    public SimpleChatService(@Qualifier("ollamaChatClientBuilder") ChatClient.Builder ollamaBuilder,
                             @Qualifier("geminiChatClientBuilder") ChatClient.Builder geminiBuilder) {
        this.ollamaChatClient = ollamaBuilder.build();
        this.geminiChatClient = geminiBuilder.build();
    }

    /**
     * Ollama로 질문
     */
    public String askOllama(String question) {
        return ollamaChatClient.prompt()
                .user(question)
                .call() // LLM 호출 (동기, blocking)
                .content(); // 응답 내용 반환
    }

    /**
     * Gemini로 질문
     */
    public String askGemini(String question) {
        return geminiChatClient.prompt()
                .user(question)
                .call()
                .content();
    }

}
