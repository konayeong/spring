package com.nhnacademy.springsecurityfinal.controller;

import com.nhnacademy.springsecurityfinal.service.TelegramSender;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class AuthController {
    private final TelegramSender telegramSender;

    // 로그인 후 로그인 페이지 접근 불가능
    @GetMapping("/auth/login")
    public String login(Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated()) {
            return "redirect:/"; // 로그인 o -> home
        }
        return "login";
    }

    @GetMapping("/auth/login/block/{id}")
    public String block(@PathVariable("id") String id) {
        telegramSender.send("[차단] 로그인 실패 5회 초과 ID : " + id);
        return "block";
    }
}
