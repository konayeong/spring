package com.nhnacademy.springmvcfinal.common.interceptor;

import com.nhnacademy.springmvcfinal.domain.user.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.servlet.HandlerInterceptor;

public class AdminCheckInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession(false);

        if(session == null) {
            User user = (User) session.getAttribute("loginUser");
            if(user.getRole().getValue() != 0) {
                // 이전 페이지 URL
                String referer = request.getHeader("Referer");
                if(referer != null) {
                    response.sendRedirect(referer);
                } else {
                    response.sendRedirect(request.getContextPath() + "/cs/");
                }
                return false;
            }
        }
        return true;
    }
}
