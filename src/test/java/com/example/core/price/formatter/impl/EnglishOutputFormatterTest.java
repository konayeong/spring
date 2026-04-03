package com.example.core.price.formatter.impl;

import com.example.core.price.domain.Price;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EnglishOutputFormatterTest {

    private final EnglishOutputFormatter formatter = new EnglishOutputFormatter();

    @Test
    void format_success() {
        Price price = new Price(1L, "동두천시" , "가정용" ,1L, 1L, 20L, 690L, null);


        String result = formatter.format(price, 10);

        assertEquals(
                "city: 동두천시, sector: 가정용, unit price(won): 690, bill total(won): 6900",
                result
        );
    }

    @Test
    void format_null_price() {
        String result = formatter.format(null, 10);

        assertEquals("[No Information]", result);
    }
}