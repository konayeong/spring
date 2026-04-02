package com.example.core.price.formatter.impl;

import com.example.core.price.domain.Price;
import com.example.core.price.formatter.OutPutFormatter;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@Profile(value = {"default", "kor"})
public class KoreanOutputFormatter implements OutPutFormatter {
    @Override
    public String format(Price price, int use) {
        if(Objects.isNull(price)) {
            return "[정보없음]";
        }

        String city = price.getCity();
        String sector = price.getSector();
        long unitPrice = price.getIntervalPrice();
        long totalPrice = price.getIntervalPrice() * use;

        return String.format("지자체명: %s, 업종: %s, 구간금액(원): %d, 총금액(원): %d", city, sector, unitPrice, totalPrice);
    }
}
