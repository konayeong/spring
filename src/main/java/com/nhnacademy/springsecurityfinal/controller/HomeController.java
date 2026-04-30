package com.nhnacademy.springsecurityfinal.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class HomeController {

    @GetMapping("/")
    public ModelAndView home(@AuthenticationPrincipal UserDetails userDetails){
        ModelAndView home = new ModelAndView("home");
        if(userDetails != null) {
            home.addObject("loginName", userDetails.getUsername());
            return home;
        }

        return home;
    }
}