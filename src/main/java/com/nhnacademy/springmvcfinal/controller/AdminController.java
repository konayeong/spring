package com.nhnacademy.springmvcfinal.controller;

import com.nhnacademy.springmvcfinal.service.InquiryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("/cs")
@RequiredArgsConstructor
public class AdminController {

    private final InquiryService inquiryService;

    @GetMapping("/admin")
    public String getInquiresAdmin(Model model) {

        return "adminMain";
    }
}
