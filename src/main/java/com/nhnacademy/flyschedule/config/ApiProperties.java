package com.nhnacademy.flyschedule.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * application.yml에 있는 설정값을 타입 안전하게 관리하기 위함
 */
@Data
@Component
@ConfigurationProperties(prefix = "data-go-kr.api")
public class ApiProperties {
    private String url;
    private String serviceKey;
}
