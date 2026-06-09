package com.nhnacademy.flyschedule.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class ChatClientConfig {

    /**
     * .yml 설정을 보고 자동으로 생성한 Ollama ChatModel을 주입받음
     * @return
     * - ChatClient.Builder
     *   - 유연성 : 필요할 때마다 새 ChatClient 생성 가능
     *   - 설정 재사용 : defaultTools, defaultAdvisors를 한 번만 설정
     *   - 테스트 용이성 : Mock ChatClient로 쉽게 교체 가능
     * - ChatClient : 서비스 내부에서 항상 동일한 설정 사용
     *
     */
    @Bean(name = "ollamaChatClientBuilder")
    @Primary
    public ChatClient.Builder ollamaChatClientBuilder(@Qualifier("ollamaChatModel") ChatModel ollamaChatModel) {
        return ChatClient.builder(ollamaChatModel);
    }

    @Bean(name = "geminiChatClientBuilder")
    public ChatClient.Builder geminiChatClientBuilder(@Qualifier("googleGenAiChatModel") ChatModel geminiChatModel) {
        return ChatClient.builder(geminiChatModel);
    }

}
