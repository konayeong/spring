package com.example.core.price.formatter.impl;

import com.example.core.price.domain.Price;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class KoreanOutputFormatterTest {

    private final KoreanOutputFormatter formatter = new KoreanOutputFormatter();

    @Test
    void format_success() {
        Price price = new Price(1L, "동두천시" , "가정용" ,1L, 1L, 20L, 690L, null);

        String result = formatter.format(price, 10);

        assertEquals(
                "지자체명: 동두천시, 업종: 가정용, 구간금액(원): 690, 총금액(원): 6900",
                result
        );
    }

    @Test
    void format_null_price() {
        String result = formatter.format(null, 10);

        assertEquals("[정보없음]", result);
    }
}