package com.nhnacademy.flyschedule.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
@RequiredArgsConstructor
public class RestClientConfig {
    private final ApiProperties apiProperties;

    @Bean
    public RestClient dataGoKrRestClient() {
        return RestClient.builder()
                .baseUrl(apiProperties.getUrl())
                .build();
    }
}
