package com.example.core.price.formatter;

import com.example.core.price.domain.Price;

public interface OutPutFormatter {
    String format(Price price, int use); // use:사용량
}
