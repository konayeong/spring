package com.example.core.common.parser;

import com.example.core.account.domain.Account;
import com.example.core.price.domain.Price;

import java.util.List;

public interface DataParser {
    List<Account> accounts();
    List<Price> prices();
}
