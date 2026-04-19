package com.nhnacademy.springmvcfinal.controller;

import com.nhnacademy.springmvcfinal.domain.dto.resp.InquiryListResponse;
import com.nhnacademy.springmvcfinal.domain.inquiry.Category;
import com.nhnacademy.springmvcfinal.domain.user.User;
import com.nhnacademy.springmvcfinal.service.InquiryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;

import java.util.List;

@Controller
@RequestMapping("/cs")
@RequiredArgsConstructor
public class UserMainController {
    private final InquiryService inquiryService;

    @GetMapping(value = {"", "/"})
    public String getInquiries(@SessionAttribute("loginUser") User user,
                               @RequestParam(value = "category", required = false) Category category,
                               Model model) {

        List<InquiryListResponse> inquiries = inquiryService.getInquiryList(user.getUserId(), category);

        model.addAttribute("name", user.getName());
        model.addAttribute("inquiries", inquiries);
        model.addAttribute("categories", Category.values());
        model.addAttribute("selectedCategory", category);
        return "userMain";
    }
}
