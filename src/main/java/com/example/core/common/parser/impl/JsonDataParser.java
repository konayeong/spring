package com.example.core.common.parser.impl;

import com.example.core.account.domain.Account;
import com.example.core.common.properties.FileProperties;
import com.example.core.price.domain.Price;
import com.example.core.common.parser.DataParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Slf4j
@Component
@ConditionalOnProperty(name = "file.type", havingValue = "json")
public class JsonDataParser implements DataParser {

    private final FileProperties fileProperties;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public JsonDataParser(FileProperties fileProperties) {
        this.fileProperties = fileProperties;
    }


    @Override
    public List<Account> accounts() {
        try {
            File file = new File(fileProperties.getAccountPath());
            return objectMapper.readValue(file, new TypeReference<List<Account>>() {});
        } catch (IOException e) {
            log.error("JSON Account 파싱 실패", e);
            throw new RuntimeException("account.json 파싱 실패");
        }
    }

    @Override
    public List<Price> prices() {
       try {
           File file = new File(fileProperties.getPricePath());
           return objectMapper.readValue(file, new TypeReference<List<Price>>() {});
       } catch (IOException e) {
           log.error("JSON Price 파싱 실패", e);
           throw new RuntimeException("Tariff.json 파싱 실패");
       }
    }

}
