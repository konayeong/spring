package com.example.core.price.formatter.impl;

import com.example.core.price.domain.Price;
import com.example.core.price.formatter.OutPutFormatter;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@Profile(value = "eng")
public class EnglishOutputFormatter implements OutPutFormatter {
    @Override
    public String format(Price price, int use) {
        if(Objects.isNull(price)) {
            return "[No Information]";
        }

        String city = price.getCity();
        String sector = price.getSector();
        long unitPrice = price.getIntervalPrice();
        long totalPrice = price.getIntervalPrice() * use;

        return String.format("city: %s, sector: %s, unit price(won): %d, bill total(won): %d", city, sector, unitPrice, totalPrice);
    }
}
