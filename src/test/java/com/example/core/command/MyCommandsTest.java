package com.example.core.command;

import com.example.core.account.domain.Account;
import com.example.core.account.service.AuthenticationService;
import com.example.core.common.exception.auth.AuthenticationException;
import com.example.core.common.exception.auth.NotLoggedInException;
import com.example.core.common.exception.price.NotFoundCitiesException;
import com.example.core.common.exception.price.NotFoundPriceException;
import com.example.core.common.exception.price.NotFoundSectorsException;
import com.example.core.price.domain.Price;
import com.example.core.price.service.PriceService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MyCommandsTest {

    @Mock
    private AuthenticationService authenticationService;

    @Mock
    private PriceService priceService;

    @InjectMocks
    private MyCommands myCommands;

    @Test
    void login_success() {
        Account account = new Account(1L, "1", "user1");
        when(authenticationService.login(1L, "1")).thenReturn(account);

        String result = myCommands.login(1L, "1");

        assertThat(result)
                .isEqualTo("Account(id=1, password=1, name=user1)");
    }

    @Test
    void login_fail() {
        when(authenticationService.login(1L, "1"))
                .thenThrow(new AuthenticationException());

        String result = myCommands.login(1L, "1");

        assertThat(result).isEqualTo("로그인 실패 : 비밀번호 불일치");
    }

    @Test
    void logout_success() {
        doNothing().when(authenticationService).logout();

        String result = myCommands.logout();

        assertThat(result).isEqualTo("good bye");
    }

    @Test
    void logout_fail() {
        doThrow(new NotLoggedInException())
                .when(authenticationService).logout();

        String result = myCommands.logout();

        assertThat(result).isEqualTo("현재 로그인된 사용자가 없습니다.");
    }


    @Test
    void currentUser_success() {
        Account account = new Account(1L, "1", "user1");
        when(authenticationService.currentUser()).thenReturn(account);

        String result = myCommands.currentUser();

        assertThat(result)
                .isEqualTo("Account(id=1, password=1, name=user1)");
    }

    @Test
    void currentUser_fail() {
        when(authenticationService.currentUser())
                .thenThrow(new NotLoggedInException());

        String result = myCommands.currentUser();

        assertThat(result).isEqualTo("현재 로그인된 사용자가 없습니다.");
    }

    @Test
    void city_success() {
        when(priceService.cities())
                .thenReturn(List.of("SEOUL", "BUSAN"));

        String result = myCommands.city();

        assertThat(result).isEqualTo("[SEOUL, BUSAN]");
    }

    @Test
    void city_fail() {
        when(priceService.cities())
                .thenThrow(new NotFoundCitiesException());

        String result = myCommands.city();

        assertThat(result).isEqualTo("등록된 지자체가 없습니다.");
    }

    @Test
    void sector_success() {
        when(priceService.sectors("SEOUL"))
                .thenReturn(List.of("A", "B"));

        String result = myCommands.sector("SEOUL");

        assertThat(result).isEqualTo("[A, B]");
    }

    @Test
    void sector_fail() {
        when(priceService.sectors("SEOUL"))
                .thenThrow(new NotFoundSectorsException("SEOUL"));

        String result = myCommands.sector("SEOUL");

        assertThat(result).isEqualTo("city:SEOUL에 등록된 업종 목록이 존재하지 않습니다.");
    }

    @Test
    void price_success() {
        Price price = new Price(1L, "SEOUL", "A",
                1L, 0L, 100L, 10L, 1000L);

        when(priceService.price("SEOUL", "A")).thenReturn(price);

        String result = myCommands.price("SEOUL", "A");

        assertThat(result)
                .isEqualTo("Price(id=1, city=SEOUL, sector=A, unitPrice=10)");
    }

    @Test
    void price_fail() {
        when(priceService.price("SEOUL", "A"))
                .thenThrow(new NotFoundPriceException("SEOUL", "A"));

        String result = myCommands.price("SEOUL", "A");

        assertThat(result).isEqualTo("city:SEOUL, sector:A 구간금액 정보가 없습니다.");
    }

    @Test
    void billTotal_success() {
        when(priceService.billTotal("SEOUL", "A", 100))
                .thenReturn("1000원");

        String result = myCommands.billTotal("SEOUL", "A", 100);

        assertThat(result).isEqualTo("1000원");
    }
}