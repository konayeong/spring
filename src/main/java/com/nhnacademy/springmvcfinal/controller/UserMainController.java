package com.nhnacademy.springmvcfinal.controller;

import com.nhnacademy.springmvcfinal.domain.inquiry.Category;
import com.nhnacademy.springmvcfinal.domain.inquiry.Inquiry;
import com.nhnacademy.springmvcfinal.domain.user.User;
import com.nhnacademy.springmvcfinal.service.InquiryService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/cs")
@RequiredArgsConstructor
public class UserMainController {
    private final InquiryService inquiryService;

    @GetMapping(value = {"", "/"})
    public String getInquiries(@RequestParam(value = "category", required = false) Category category,
                               HttpServletRequest req,
                               Model model) {
        HttpSession session = req.getSession(false);
        User user = (User) session.getAttribute("loginUser");

        List<Inquiry> inquiries = inquiryService.getInquiryList(user.getUserId(), category);

        model.addAttribute("name", user.getName());
        model.addAttribute("hide", false);
        model.addAttribute("inquiries", inquiries);
        return "userMain";
    }
}
