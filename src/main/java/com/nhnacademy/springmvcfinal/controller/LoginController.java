package com.nhnacademy.springmvcfinal.controller;

import com.nhnacademy.springmvcfinal.domain.user.Role;
import com.nhnacademy.springmvcfinal.domain.user.User;
import com.nhnacademy.springmvcfinal.domain.dto.LoginRequest;
import com.nhnacademy.springmvcfinal.exception.ValidationFailedException;
import com.nhnacademy.springmvcfinal.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/cs/login")
@RequiredArgsConstructor
public class LoginController {

    private final UserService userService;

    @GetMapping
    public String loginForm(HttpServletRequest req) {
        HttpSession session = req.getSession(false);
        // TODO 관리자 interceptor로 처리하기
        if(session != null) {
            User user = (User) session.getAttribute("loginUser");
            if (user != null) {
                if(user.getRole().equals(Role.USER)) {
                    return "redirect:/cs";
                }return "redirect:/cs/admin";
            }
        }
        return "loginForm";
    }

    @PostMapping
    public String doLogin(HttpServletRequest req,
                          @Valid LoginRequest request,
                          BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new ValidationFailedException();
        }

        User user = userService.doLogin(request.getId(), request.getPwd());

        // 로그인 성공
        HttpSession session = req.getSession(true);
        session.setAttribute("loginUser", user);

        if(user.getRole().equals(Role.USER)) {
            return "redirect:/cs/";
        }
        return "redirect:/cs/admin";
        // TODO 아이디 비밀번호 입력 오류를 error 페이지로 이동할 필요가 있나?
    }
}
