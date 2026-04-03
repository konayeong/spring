package com.example.core.account.service;

import com.example.core.account.domain.Account;
import com.example.core.common.exception.auth.AccountNotFoundException;
import com.example.core.common.exception.auth.AuthenticationException;
import com.example.core.common.exception.auth.NotLoggedInException;
import com.example.core.common.parser.DataParser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthenticationServiceTest {

    @Mock
    private DataParser dataParser;

    @InjectMocks
    private AuthenticationService authenticationService;

    @BeforeEach
    void setUp() {
        List<Account> mockAccounts = List.of(
                new Account(1L, "1234", "user1"),
                new Account(2L, "abcd", "user2")
        );

        when(dataParser.accounts()).thenReturn(mockAccounts);

        authenticationService.loadAccounts();
    }

    @Test
    void login_success() {
        Account account = authenticationService.login(1L, "1234");

        assertNotNull(account);
        assertEquals("user1", account.getName());
    }

    @Test
    void login_fail_account_not_found() {
        assertThrows(AccountNotFoundException.class,
                () -> authenticationService.login(999L, "1234"));
    }

    @Test
    void login_fail_wrong_password() {
        assertThrows(AuthenticationException.class,
                () -> authenticationService.login(1L, "wrong"));
    }

    @Test
    void logout_success() {
        authenticationService.login(1L, "1234");

        assertDoesNotThrow(() -> authenticationService.logout());
    }

    @Test
    void logout_fail_not_logged_in() {
        assertThrows(NotLoggedInException.class,
                () -> authenticationService.logout());
    }

    @Test
    void currentUser_when_logged_in() {
        authenticationService.login(1L, "1234");

        Account current = authenticationService.currentUser();

        assertNotNull(current);
        assertEquals(1L, current.getId());
    }

    @Test
    void currentUser_when_not_logged_in() {
        assertThrows(NotLoggedInException.class,
                () -> authenticationService.currentUser());
    }
}