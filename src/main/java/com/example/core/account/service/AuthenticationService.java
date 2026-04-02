package com.example.core.account.service;

import com.example.core.account.domain.Account;
import com.example.core.common.exception.auth.AccountNotFoundException;
import com.example.core.common.exception.auth.AuthenticationException;
import com.example.core.common.exception.auth.NotLoggedInException;
import com.example.core.common.parser.DataParser;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Slf4j
@Component
public class AuthenticationService {
    private final Map<Long, Account> accountMap = new HashMap<>(); // account 저장
    private final DataParser dataParser;
    private Account currentUser;

    public AuthenticationService(DataParser dataParser) {
        this.dataParser = dataParser;
    }

    // 사용 전 map에 저장
    @PostConstruct
    private void loadAccounts() {
        List<Account> accounts = dataParser.accounts();
        for (Account account : accounts) {
            accountMap.putIfAbsent(account.getId(), account); // 중복 x
        }
        log.info("Accounts : {}", accountMap.keySet());
    }

    // 로그인
    public Account login(Long id, String password) {
        Account account = accountMap.get(id);

        // 회원 존재 x
        if(Objects.isNull(account)) {
            log.debug("ID : {} 회원이 존재하지 않습니다.", id);
            throw new AccountNotFoundException();
        }

        // 비밀번호 불일치
        if(!account.getPassword().equals(password)) {
            log.debug("ID : {} 비밀번호 불일치", id);
            throw new AuthenticationException();
        }

        log.info("로그인 성공 {}", account.getName());
        currentUser = account;
        return account;
    }

    // 로그아웃
    public void logout() {
        if(Objects.isNull(currentUser)) {
            log.debug("로그인 사용자 없음");
            throw new NotLoggedInException();
        }
        log.info("로그아웃 성공");
        currentUser = null;
    }

    // 현재 로그인한 회원
    public Account currentUser() {
        if(Objects.isNull(currentUser)) {
            log.debug("로그인 사용자 없음");
        }
        return currentUser;
    }
}
