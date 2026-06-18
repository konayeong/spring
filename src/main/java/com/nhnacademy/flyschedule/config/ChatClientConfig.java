package com.nhnacademy.flyschedule.config;

import com.nhnacademy.flyschedule.tool.AirlineInfoTool;
import com.nhnacademy.flyschedule.tool.AirportInfoTool;
import com.nhnacademy.flyschedule.tool.FlightSearchTool;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

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
     */
    @Bean(name = "ollamaChatClientBuilder")
    @Primary
    public ChatClient.Builder ollamaChatClientBuilder(@Qualifier("ollamaChatModel") ChatModel ollamaChatModel,
                                                      FlightSearchTool flightSearchTool,
                                                      AirportInfoTool airportInfoTool,
                                                      AirlineInfoTool airlineInfoTool) {
        return ChatClient.builder(ollamaChatModel)
                .defaultTools(flightSearchTool, airportInfoTool, airlineInfoTool)
                .defaultAdvisors(new SimpleLoggerAdvisor(), chatLoggingAdvisor);
    }

    @Bean(name = "geminiChatClientBuilder")
    public ChatClient.Builder geminiChatClientBuilder(@Qualifier("googleGenAiChatModel") ChatModel geminiChatModel,
                                                      FlightSearchTool flightSearchTool,
                                                      AirportInfoTool airportInfoTool,
                                                      AirlineInfoTool airlineInfoTool) {
        return ChatClient.builder(geminiChatModel)
                .defaultTools(flightSearchTool, airportInfoTool, airlineInfoTool)
                .defaultAdvisors(new SimpleLoggerAdvisor(), chatLoggingAdvisor);
    }

    /**
     * Tool 호출 없는
     */
    @Bean(name = "ollamaAnalysisClientBuilder")
    public ChatClient.Builder ollamaAnalysisClientBuilder(@Qualifier("ollamaChatModel") ChatModel ollamaChatModel) {
        return ChatClient.builder(ollamaChatModel)
                .defaultAdvisors(new SimpleLoggerAdvisor(), chatLoggingAdvisor);
    }

    @Bean(name = "geminiAnalysisClientBuilder")
    public ChatClient.Builder geminiAnalysisClientBuilder(@Qualifier("googleGenAiChatModel") ChatModel geminiChatModel) {
        return ChatClient.builder(geminiChatModel)
                .defaultAdvisors(new SimpleLoggerAdvisor(), chatLoggingAdvisor);
    }
}
