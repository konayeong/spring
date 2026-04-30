package com.nhnacademy.springsecurityfinal.config.handler;

import com.nhnacademy.springsecurityfinal.exception.MemberNotFoundException;
import com.nhnacademy.springsecurityfinal.service.LoginService;
import com.nhnacademy.springsecurityfinal.service.TelegramSender;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class LoginFailedHandler implements AuthenticationFailureHandler {

    private final LoginService loginService;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception)
            throws IOException, ServletException {
        String id = request.getParameter("id");

        if(exception.getCause() instanceof MemberNotFoundException) { // 없는 계정 로그인 실패 -> count(x)
            log.info("없는 계정 로그인 실패");
        }else if(loginService.isBlocked(id)) {
            log.warn("로그인 실패 5회 초과");
            response.sendRedirect("/auth/login/block/" + id);
            return;
        }else {
            loginService.fail(id);
        }

        response.sendRedirect("/auth/login?error=true");
    }
}