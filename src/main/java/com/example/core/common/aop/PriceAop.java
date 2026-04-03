package com.example.core.common.aop;

import com.example.core.account.domain.Account;
import com.example.core.account.service.AuthenticationService;
import com.example.core.common.exception.auth.NotLoggedInException;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class PriceAop {
    private final AuthenticationService authenticationService;

    public PriceAop(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @Pointcut("@annotation(com.example.core.common.aop.PriceLog)")
    public void log() {
    }

    // 로그인 체크
    @Before("log()")
    public void loginCheck(JoinPoint joinPoint) {
        if(!authenticationService.isLogin()) {
            log.debug("[로그인 필요]");
            throw new NotLoggedInException();
        }else {
            Account account = authenticationService.currentUser();
            String name = account.getName();
            String className = joinPoint.getSignature().getDeclaringTypeName();
            log.info("----- {} class {}({}) ----->", name, className, joinPoint.getArgs());
        }
    }

    @AfterReturning(value = "log()", returning = "result")
    public void writeLog(JoinPoint joinPoint, Object result) {
        String name = authenticationService.currentUser().getName();
        String request = joinPoint.getSignature().getDeclaringTypeName();

        log.info("<----- {} class {}({}) -----", name, request, result);
    }
}
