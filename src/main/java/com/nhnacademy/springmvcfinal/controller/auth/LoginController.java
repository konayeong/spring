package com.nhnacademy.springmvcfinal.controller.auth;

import com.nhnacademy.springmvcfinal.domain.user.Role;
import com.nhnacademy.springmvcfinal.domain.user.User;
import com.nhnacademy.springmvcfinal.domain.dto.req.LoginRequest;
import com.nhnacademy.springmvcfinal.exception.LoginFailedException;
import com.nhnacademy.springmvcfinal.exception.ValidationFailedException;
import com.nhnacademy.springmvcfinal.service.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Locale;

@Controller
@RequestMapping("/cs/login")
@RequiredArgsConstructor
public class LoginController {

    private final UserService userService;
    private final MessageSource messageSource; // 다국어

    @GetMapping
    public String loginForm() {
        return "loginForm";
    }

    @PostMapping
    // HttpSession : req.getSession(true), Spring이 자동 주입
    public String doLogin(HttpSession session,
                          @Valid LoginRequest request,
                          BindingResult bindingResult,
                          Model model,
                          Locale locale) {
        if (bindingResult.hasErrors()) {
            throw new ValidationFailedException();
        }

        try{
            User user = userService.doLogin(request.getId(), request.getPwd());
            // 로그인 성공
            session.setAttribute("loginUser", user);

            return user.getRole() == Role.USER ? "redirect:/cs/" : "redirect:/cs/admin";
        } catch (LoginFailedException e) {
            String errorMsg = messageSource.getMessage("login.error", null, locale);
            model.addAttribute("error", errorMsg);
            return "loginForm";
        }
    }
}
