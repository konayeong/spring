package com.example.core.command;

import com.example.core.account.domain.Account;
import com.example.core.account.service.AuthenticationService;
import com.example.core.common.aop.PriceLog;
import com.example.core.price.domain.Price;
import com.example.core.price.service.PriceService;
import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

import java.util.List;
import java.util.Objects;

@ShellComponent
@RequiredArgsConstructor
public class MyCommands {
    private final AuthenticationService authenticationService;
    private final PriceService priceService;

    @ShellMethod(key = "login", value = "id, password") // help 입력 시 출력
    public String login(Long id, String password) {
        Account account = authenticationService.login(id, password);
        return "Account(id=" + id + ", password=" + password + ", name=" + account.getName() + ")";
    }

    @ShellMethod
    public String logout() {
        authenticationService.logout();
        return "good bye";
    }

    @ShellMethod
    public String currentUser() {
        Account account = authenticationService.currentUser();
        if(Objects.isNull(account)) {
            return "로그인 사용자 없음";
        }
        return "Account(id=" + account.getId() + ", password=" + account.getPassword() + ", name=" + account.getName() + ")";
    }

    @PriceLog
    @ShellMethod
    public String city() {
        List<String> cities = priceService.cities();

        if(Objects.isNull(cities)) {
            return "등록된 지자체가 없습니다.";
        }

        return cities.toString();
    }

    @PriceLog
    @ShellMethod(key = "sector", value = "city")
    public String sector(String city) {
        List<String> sectors = priceService.sectors(city);

        if(Objects.isNull(sectors)) {
            return "업종 목록이 존재하지 않습니다.";
        }

        return sectors.toString();
    }

    @PriceLog
    @ShellMethod(key = "price", value = "city, sector")
    public String price(String city, String sector) {
        Price price = priceService.price(city, sector);

        if(Objects.isNull(price)) {
            return "구간금액 정보가 없습니다.";
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
