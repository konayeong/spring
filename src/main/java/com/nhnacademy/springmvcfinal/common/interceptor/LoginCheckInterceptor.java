package com.nhnacademy.springmvcfinal.common.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.IOException;

public class LoginCheckInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest req,
                             HttpServletResponse resp,
                             Object handler) throws IOException {

        HttpSession session = req.getSession(false);

        if (session != null && session.getAttribute("loginUser") != null) {
            return true;
        }

        resp.sendRedirect("/cs/login");
        return false;
    }
}
