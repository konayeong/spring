package com.example.core.common.properties;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@AllArgsConstructor
@ConfigurationProperties("file")
public class FileProperties {
    private String type;
    private String pricePath;
    private String accountPath;
}
