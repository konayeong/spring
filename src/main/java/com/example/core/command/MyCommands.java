package com.example.core.command;

import com.example.core.account.domain.Account;
import com.example.core.account.service.AuthenticationService;
import com.example.core.common.aop.PriceLog;
import com.example.core.common.exception.price.NotFoundCitiesException;
import com.example.core.common.exception.auth.AccountNotFoundException;
import com.example.core.common.exception.auth.AuthenticationException;
import com.example.core.common.exception.auth.NotLoggedInException;
import com.example.core.common.exception.price.NotFoundPriceException;
import com.example.core.common.exception.price.NotFoundSectorsException;
import com.example.core.price.domain.Price;
import com.example.core.price.service.PriceService;
import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

import java.util.List;

@ShellComponent
@RequiredArgsConstructor
public class MyCommands {
    private final AuthenticationService authenticationService;
    private final PriceService priceService;

    @ShellMethod(key = "login", value = "id, password") // help 입력 시 출력
    public String login(Long id, String password) {
        Account account;
        try{
            account = authenticationService.login(id, password);
        }catch (AccountNotFoundException | AuthenticationException e) {
            return e.getMessage();
        }
        return "Account(id=" + id + ", password=" + password + ", name=" + account.getName() + ")";
    }

    @ShellMethod
    public String logout() {
        try{
            authenticationService.logout();
        }catch (NotLoggedInException e) {
            return e.getMessage();
        }
        return "good bye";
    }

    @ShellMethod
    public String currentUser() {
        Account account;
        try{
            account = authenticationService.currentUser();
        }catch (NotLoggedInException e) {
            return e.getMessage();
        }

        return "Account(id=" + account.getId() + ", password=" + account.getPassword() + ", name=" + account.getName() + ")";
    }

    @PriceLog
    @ShellMethod
    public String city() {
        List<String> cities;

        try{
            cities = priceService.cities();
        }catch (NotFoundCitiesException e) {
            return e.getMessage();
        }

        return cities.toString();
    }

    @PriceLog
    @ShellMethod(key = "sector", value = "city")
    public String sector(String city) {
        List<String> sectors;

        try{
            sectors = priceService.sectors(city);
        }catch (NotFoundSectorsException e) {
            return e.getMessage();
        }

        return sectors.toString();
    }

    @PriceLog
    @ShellMethod(key = "price", value = "city, sector")
    public String price(String city, String sector) {
        Price price;

        try{
            price = priceService.price(city, sector);
        }catch (NotFoundPriceException e) {
            return e.getMessage();
        }

        return String.format("Price(id=%d, city=%s, sector=%s, unitPrice=%d)",
                price.getId(), city, sector, price.getIntervalPrice());
    }

    @PriceLog
    @ShellMethod(key = "bill-total", value = "city, sector, use")
    public String billTotal(String city, String sector, int use) {
        return priceService.billTotal(city, sector, use);
    }

}
