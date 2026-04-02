package com.example.core.common.parser.impl;

import com.example.core.account.domain.Account;
import com.example.core.common.properties.FileProperties;
import com.example.core.price.domain.Price;
import com.example.core.common.parser.DataParser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
@ConditionalOnProperty(name = "file.type", havingValue = "json")
public class JsonDataParser implements DataParser {

    private FileProperties fileProperties;

    @Override
    public List<Account> accounts() {

        return List.of();
    }

    @Override
    public List<Price> prices() {
        return List.of();
    }

}
