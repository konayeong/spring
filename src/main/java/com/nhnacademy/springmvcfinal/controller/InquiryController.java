package com.nhnacademy.springmvcfinal.controller;

import com.nhnacademy.springmvcfinal.domain.inquiry.Inquiry;
import com.nhnacademy.springmvcfinal.service.InquiryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/cs")
@RequiredArgsConstructor
public class InquiryController {

    private final InquiryService inquiryService;

    @ModelAttribute("inquiry")
    public Inquiry getInquiry(@PathVariable("inquiryId") int inquiryId) {
        return inquiryService.getInquiry(inquiryId);
    }

    @GetMapping("/inquiries/{inquiryId}")
    public String inquiryDetail() {
        return "inquiryDetail";
    }
}
