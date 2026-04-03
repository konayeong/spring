package com.example.core.common.properties;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ConfigurationProperties("file")
public class FileProperties {
    private String type;
    private String pricePath;
    private String accountPath;
}
