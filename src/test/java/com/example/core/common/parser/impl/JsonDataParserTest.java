package com.example.core.common.parser.impl;

import com.example.core.account.domain.Account;
import com.example.core.common.exception.ParsingException;
import com.example.core.common.properties.FileProperties;
import com.example.core.price.domain.Price;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

class JsonDataParserTest {

    @TempDir
    File tempDir;

    @Test
    void accounts_parse_success() throws Exception {
        File file = new File(tempDir, "account.json");

        String json = """
                [
                  {"아이디": 1, "비밀번호" : 1, "이름": "선도형"},
                  {"아이디": 2, "비밀번호" : 2, "이름": "sando"}
                ]
                """;

        try (FileWriter writer = new FileWriter(file)) {
            writer.write(json);
        }

        FileProperties fileProperties = new FileProperties();
        fileProperties.setAccountPath(file.getAbsolutePath());

        JsonDataParser parser = new JsonDataParser(fileProperties);

        List<Account> accounts = parser.accounts();

        assertThat(accounts).hasSize(2);
        assertThat(accounts.getFirst().getId()).isEqualTo(1L);
        assertThat(accounts.getFirst().getName()).isEqualTo("선도형");
    }

    @Test
    void accounts_parse_fail() {
        FileProperties fileProperties =
                new FileProperties("json", null, "invalid.json");

        JsonDataParser parser = new JsonDataParser(fileProperties);

        assertThatThrownBy(parser::accounts)
                .isInstanceOf(ParsingException.class);
    }

    @Test
    void prices_parse_success() throws IOException {
        File file = new File(tempDir, "price.json");

        String json = """
                [
                  {
                    "순번": 1,
                    "지자체명": "동두천시",
                    "업종": "가정용",
                    "단계": 1,
                    "구간시작(세제곱미터)": 1,
                    "구간끝(세제곱미터)": 20,
                    "구간금액(원)": 690,
                    "단계별 기본요금(원)": ""
                  },
                  {
                    "순번": 2,
                    "지자체명": "동두천시",
                    "업종": "가정용",
                    "단계": 2,
                    "구간시작(세제곱미터)": 21,
                    "구간끝(세제곱미터)": 30,
                    "구간금액(원)": 1090,
                    "단계별 기본요금(원)": ""
                  }
                ]
                """;

        try (FileWriter writer = new FileWriter(file)) {
            writer.write(json);
        }

        FileProperties fileProperties = new FileProperties();
        fileProperties.setPricePath(file.getAbsolutePath());

        JsonDataParser parser = new JsonDataParser(fileProperties);

        List<Price> prices = parser.prices();

        assertThat(prices).hasSize(2);
        assertThat(prices.getFirst().getId()).isEqualTo(1L);
        assertThat(prices.getFirst().getCity()).isEqualTo("동두천시");
        assertThat(prices.getFirst().getSector()).isEqualTo("가정용");
    }
}