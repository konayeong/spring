package com.nhnacademy.flyschedule.config;

import com.nhnacademy.flyschedule.tool.AiTool;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import java.util.List;

@Configuration
@RequiredArgsConstructor
public class ChatClientConfig {
    private final ChatLoggingAdvisor chatLoggingAdvisor;
    /**
     * .yml 설정을 보고 자동으로 생성한 Ollama ChatModel을 주입받음
     * @return
     * - ChatClient.Builder
     *   - 유연성 : 필요할 때마다 새 ChatClient 생성 가능
     *   - 설정 재사용 : defaultTools, defaultAdvisors를 한 번만 설정
     *   - 테스트 용이성 : Mock C
     * - ChatClient : 서비스 내부에서 항상 동일한 설정 사용
     *
     */
    @Bean(name = "ollamaChatClientBuilder")
    @Primary
    public ChatClient.Builder ollamaChatClientBuilder(@Qualifier("ollamaChatModel") ChatModel ollamaChatModel,
                                                      List<AiTool> aiTools) {
        return ChatClient.builder(ollamaChatModel)
                .defaultTools(aiTools.toArray(new Object[0]))
                .defaultAdvisors(new SimpleLoggerAdvisor(), chatLoggingAdvisor);
    }

    @Bean(name = "geminiChatClientBuilder")
    public ChatClient.Builder geminiChatClientBuilder(@Qualifier("googleGenAiChatModel") ChatModel geminiChatModel,
                                                      List<AiTool> aiTools) {
        return ChatClient.builder(geminiChatModel)
                .defaultTools(aiTools.toArray(new Object[0]))
                .defaultAdvisors(new SimpleLoggerAdvisor(), chatLoggingAdvisor);
    }

}
