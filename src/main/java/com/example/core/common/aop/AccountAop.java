package com.example.core.common.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class AccountAop {

    @Pointcut("execution(* com.example.core.account.service.AuthenticationService.login(..))")
    public void login() {
    }

    @Pointcut(value = "args(id,password,..)", argNames = "id,password")
    public void loginArgs(Long id, String password) {
    }

    @Pointcut("execution(* com.example.core.account.service.AuthenticationService.logout())")
    public void logout() {
    }

    @Before(value = "login() && loginArgs(id, password)", argNames = "id,password")
    public void loginLog(Long id, String password) {
        log.info("login([{}, {}])", id, password);
    }

    @Before("logout()")
    public void logoutLog() {
        log.info("logout([])");
    }

}
