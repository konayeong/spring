package com.example.core.common.parser.impl;

import com.example.core.account.domain.Account;
import com.example.core.common.properties.FileProperties;
import com.example.core.price.domain.Price;
import com.example.core.common.parser.DataParser;
import com.opencsv.bean.CsvToBeanBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
@ConditionalOnProperty(name = "file.type", havingValue = "csv")
public class CsvDataParser implements DataParser {

    private final FileProperties fileProperties;

    @Override
    public List<Account> accounts() {
        File targetFile = new File(fileProperties.getAccountPath());

        try(BufferedReader br = new BufferedReader(new FileReader(targetFile))) {
            // CSVToBeanBuilder : Reader 기반으로 CSV 읽음
            return new CsvToBeanBuilder<Account>(br)
                    .withType(Account.class)
                    .build()
                    .parse();
        } catch (IOException e) {
            log.error("CSV Account 파싱 실패", e);
            throw new RuntimeException("account.csv 파싱 실패",e);
        }
    }

    /*
    return parser.getRecords().stream()
        .map(record -> new Account(
                Long.parseLong(record.get("id")),
                record.get("name"),
                Integer.parseInt(record.get("balance"))
        ))
        .toList();
    @Override
public List<Account> accounts() {
    File targetFile = new File(fileProperties.getAccountPath());

    try (BufferedReader br = new BufferedReader(new FileReader(targetFile))) {

        CSVParser parser = CSVFormat.DEFAULT
                .withFirstRecordAsHeader() // 첫 줄 header 사용
                .parse(br);

        List<Account> accounts = new ArrayList<>();

        for (CSVRecord record : parser) {
            Account account = new Account();

            account.setId(Long.parseLong(record.get("id")));
            account.setName(record.get("name"));
            account.setBalance(Integer.parseInt(record.get("balance")));

            accounts.add(account);
        }

        return accounts;

    } catch (IOException e) {
        log.error("CSV Account 파싱 실패", e);
        throw new RuntimeException("account.csv 파싱 실패", e);
    }
}
     */

    @Override
    public List<Price> prices() {
        File targetFile = new File(fileProperties.getPricePath());

        try(BufferedReader br = new BufferedReader(new FileReader(targetFile))) {
            return new CsvToBeanBuilder<Price>(br)
                                    .withType(Price.class)
                                    .build()
                                    .parse();
        } catch (IOException e) {
            log.error("CSV Tariff(Price) 파싱 실패", e);
            throw new RuntimeException("Tariff.csv 파싱 실패");
        }
    }
}
