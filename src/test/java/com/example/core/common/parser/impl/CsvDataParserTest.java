package com.example.core.common.parser.impl;

import com.example.core.account.domain.Account;
import com.example.core.common.exception.ParsingException;
import com.example.core.common.properties.FileProperties;
import com.example.core.price.domain.Price;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.boot.test.context.SpringBootTest;
import java.io.File;
import java.io.FileWriter;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest(properties = {
        "file.type=csv",
        "spring.shell.interactive.enabled=false"
})
class CsvDataParserTest {
    @TempDir // 임시 디렉터리 생성용?
    File tempDir;

    @Test
    void accounts_parse_success() throws Exception {
        File file = new File(tempDir, "account.csv");

        String csv = """
                아이디 , 비밀번호, 이름
                1,1234,user1
                2,5678,user2
                """;

        try (FileWriter writer = new FileWriter(file)) {
            writer.write(csv);
        }

        FileProperties fileProperties = new FileProperties();
        fileProperties.setAccountPath(file.getAbsolutePath());

        CsvDataParser parser = new CsvDataParser(fileProperties);

        List<Account> accounts = parser.accounts();

        assertThat(accounts).hasSize(2);
        assertThat(accounts.getFirst().getId()).isEqualTo(1L);
        assertThat(accounts.getFirst().getPassword()).isEqualTo("1234");
        assertThat(accounts.getFirst().getName()).isEqualTo("user1");
    }

    @Test
    void accounts_parse_fail() {
        FileProperties fileProperties = new FileProperties();
        fileProperties.setAccountPath("invalid.csv");

        CsvDataParser parser = new CsvDataParser(fileProperties);

        assertThatThrownBy(parser::accounts)
                .isInstanceOf(ParsingException.class);
    }

    @Test
    void prices_parse_success() throws Exception {
        File file = new File(tempDir, "Tariff.csv");

        String csv = """
                순번 , 지자체명 , 업종 , 단계 , 구간시작(세제곱미터)  , 구간끝(세제곱미터)  , 구간금액(원)  , 단계별 기본요금(원)
                1, 동두천시 , 가정용 ,1,1,20,690,
                2, 동두천시 , 가정용 ,2,21,30,1090,
                """;

        try (FileWriter writer = new FileWriter(file)) {
            writer.write(csv);
        }

        FileProperties fileProperties = new FileProperties();
        fileProperties.setPricePath(file.getAbsolutePath());

        CsvDataParser parser = new CsvDataParser(fileProperties);

        List<Price> prices = parser.prices();

        assertThat(prices).hasSize(2);
        assertThat(prices.getFirst().getId()).isEqualTo(1L);
        assertThat(prices.getFirst().getCity()).isEqualTo("동두천시");
        assertThat(prices.getFirst().getSector()).isEqualTo("가정용");
        assertThat(prices.getFirst().getIntervalPrice()).isEqualTo(690);
    }
}