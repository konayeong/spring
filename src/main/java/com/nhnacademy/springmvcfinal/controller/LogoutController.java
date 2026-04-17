package com.nhnacademy.springmvcfinal.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Objects;

@Controller
@RequestMapping("/cs/logout")
public class LogoutController {
    @PostMapping
    public String logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);

        if(Objects.nonNull(session)) {
            session.invalidate();
        }

        return "redirect:/cs/login";
    }
}