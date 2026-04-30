package com.nhnacademy.springsecurityfinal.config.handler;

import com.nhnacademy.springsecurityfinal.service.LoginService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import java.io.IOException;

@Component
@RequiredArgsConstructor
public class LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final LoginService loginService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException, ServletException {
        String id = request.getParameter("id");
        loginService.login(id);
        // TODO 로그인 실패 카운트 5회 초과시 로그인 후 reset or 차단
        // 현재는 count reset
        super.onAuthenticationSuccess(request, response, authentication);

    }
}
