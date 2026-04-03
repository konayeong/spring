package com.example.core.common.parser.impl;

import com.example.core.account.domain.Account;
import com.example.core.common.exception.ParsingException;
import com.example.core.common.properties.FileProperties;
import com.example.core.price.domain.Price;
import com.example.core.common.parser.DataParser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import java.io.*;
import java.util.ArrayList;
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
        List<Account> accounts = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(targetFile))) {
            CSVParser parser = CSVParser.parse(br, CSVFormat.EXCEL);

            List<CSVRecord> csvRecords = parser.getRecords();

            for (int i=1; i<csvRecords.size(); i++) { // 헤더 스킵
                CSVRecord csvRecord = csvRecords.get(i);
                Long id = Long.valueOf(csvRecord.get(0));
                String password = csvRecord.get(1).trim();
                String name = csvRecord.get(2).trim();

                Account account = new Account(id, password, name);
                accounts.add(account);
            }
        } catch (Exception e) {
            log.error("CSV Account 파싱 실패", e);
            throw new ParsingException("account.csv 파싱 실패");
        }
        return accounts;
    }

    @Override
    public List<Price> prices() {
        File targetFile = new File(fileProperties.getPricePath());
        List<Price> prices = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(targetFile))) {
            CSVParser parser = CSVParser.parse(br, CSVFormat.EXCEL);

            List<CSVRecord> csvRecords = parser.getRecords();

            for (int i=1; i<csvRecords.size(); i++) {
                CSVRecord csvRecord = csvRecords.get(i);
                Long id = Long.valueOf(csvRecord.get(0));
                String city = csvRecord.get(1).trim();
                String sector = csvRecord.get(2).trim();
                Long step = Long.valueOf(csvRecord.get(3));
                Long intervalStart = Long.valueOf(csvRecord.get(4));
                Long intervalEnd = Long.valueOf(csvRecord.get(5));
                Long intervalPrice = Long.valueOf(csvRecord.get(6));
                Long basicPrice = csvRecord.get(7).isEmpty() ? 0 : Long.parseLong(csvRecord.get(7)); // 없는 데이터

                Price price = new Price(id, city, sector, step, intervalStart, intervalEnd, intervalPrice, basicPrice);
                prices.add(price);
            }
        } catch (Exception e) {
            log.error("CSV Tariff(Price) 파싱 실패", e);
            throw new ParsingException("Tariff.csv 파싱 실패");
        }
        return prices;
    }
}

