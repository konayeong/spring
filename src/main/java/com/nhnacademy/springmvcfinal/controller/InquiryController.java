package com.nhnacademy.springmvcfinal.controller;

import com.nhnacademy.springmvcfinal.domain.dto.resp.InquiryResponse;
import com.nhnacademy.springmvcfinal.service.InquiryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/cs")
@RequiredArgsConstructor
public class InquiryController {

    private final InquiryService inquiryService;

    @GetMapping("/inquiries/{inquiryId}")
    public String inquiryDetail(@PathVariable("inquiryId") int inquiryId,
                                Model model) {
        InquiryResponse inquiry = inquiryService.getInquiry(inquiryId);
        model.addAttribute("inquiry", inquiry);
        return "inquiryDetail";
    }
}
