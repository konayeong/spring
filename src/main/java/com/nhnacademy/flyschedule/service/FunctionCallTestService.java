package com.nhnacademy.flyschedule.service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

@Service
public class FunctionCallTestService {
    private final ChatClient chatClient;

    public FunctionCallTestService(ChatClient.Builder builder) {
        this.chatClient = builder.build();
    }

    public String testFunctionCalling(String userMessage) {
        return chatClient.prompt()
                .user(userMessage)
                .call()
                .content();
    }
}