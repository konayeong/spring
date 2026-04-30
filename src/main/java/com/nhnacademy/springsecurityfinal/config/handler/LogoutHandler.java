package com.nhnacademy.springsecurityfinal.config.handler;

import com.nhnacademy.springsecurityfinal.repository.RedisSecurityContextRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;
import org.springframework.stereotype.Component;
import java.io.IOException;

@Component
@RequiredArgsConstructor
public class LogoutHandler extends SimpleUrlLogoutSuccessHandler {

    private final RedisSecurityContextRepository redisSecurityContextRepository;

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, @Nullable Authentication authentication) throws IOException, ServletException {

        String sessionId = null;
        Cookie[] cookies = request.getCookies();

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("ACADEMY-SESSION".equals(cookie.getName())) {
                    sessionId = cookie.getValue();
                    break;
                }
            }
        }

        // 레디스 세션 삭제
        if (sessionId != null) {
            redisSecurityContextRepository.deleteContext(sessionId);
        }

        // 쿠키 세션 삭제
        Cookie deleteCookie = new Cookie("ACADEMY-SESSION", null);
        deleteCookie.setMaxAge(0);
        deleteCookie.setPath("/");
        response.addCookie(deleteCookie);

        super.onLogoutSuccess(request, response, authentication);
    }

}