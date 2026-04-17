package com.nhnacademy.springmvcfinal.controller;

import com.nhnacademy.springmvcfinal.domain.inquiry.Category;
import com.nhnacademy.springmvcfinal.domain.user.User;
import com.nhnacademy.springmvcfinal.domain.dto.InquiryRegisterRequest;
import com.nhnacademy.springmvcfinal.exception.ValidationFailedException;
import com.nhnacademy.springmvcfinal.service.InquiryService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/cs")
@RequiredArgsConstructor
public class InquiryRegisterController {

    private final InquiryService inquiryService;

    @GetMapping("/inquiry")
    public String registerInquiryForm(Model model) {
        model.addAttribute("categories", Category.values());
        return "inquiryRegister";
    }

    // TODO-Q userId가 필요할 때마다 세션에서 꺼내오는게 맞을까?
    @PostMapping("/inquiry")
    public String registerInquiry(HttpServletRequest req,
                               @Valid @ModelAttribute InquiryRegisterRequest request,
                               BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            throw new ValidationFailedException();
        }
        // inquiry 등록
        HttpSession session = req.getSession(false);
        User user = (User) session.getAttribute("loginUser");
        String userId = user.getUserId();

        inquiryService.registerInquiry(userId, request);

        return "redirect:/cs/";
    }
}
