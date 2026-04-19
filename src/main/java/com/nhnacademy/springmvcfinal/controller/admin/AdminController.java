package com.nhnacademy.springmvcfinal.controller.admin;

import com.nhnacademy.springmvcfinal.domain.dto.req.AnswerRegisterRequest;
import com.nhnacademy.springmvcfinal.domain.dto.resp.InquiryListResponse;
import com.nhnacademy.springmvcfinal.domain.dto.resp.InquiryResponse;
import com.nhnacademy.springmvcfinal.domain.user.User;
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
import java.util.List;

@Controller
@RequestMapping("/cs")
@RequiredArgsConstructor
public class AdminController {

    private final InquiryService inquiryService;

    @GetMapping("/admin")
    public String getInquiresAdmin(Model model) {
        List<InquiryListResponse> inquiries = inquiryService.getInquiryListNonAnswer();
        model.addAttribute("inquiries", inquiries);
        return "adminMain";
    }

    @GetMapping("/admin/answer/{inquiryId}")
    public String answerForm(@PathVariable("inquiryId") int inquiryId,
                             Model model) {
        InquiryResponse inquiry = inquiryService.getInquiryNonAnswer(inquiryId);
        model.addAttribute("inquiry", inquiry);
        return "adminAnswerForm";
    }

    @PostMapping("/admin/answer/{inquiryId}")
    public String submitAnswer(@PathVariable("inquiryId") int inquiryId,
                               @Valid @ModelAttribute AnswerRegisterRequest registerRequest,
                               BindingResult bindingResult,
                               HttpServletRequest request) {
        if(bindingResult.hasErrors()) {
            throw new ValidationFailedException();
        }

        HttpSession session = request.getSession(false);
        User user = (User) session.getAttribute("loginUser");
        String adminId = user.getUserId();

        inquiryService.addAnswer(inquiryId, adminId, registerRequest.getContent());

        return "redirect:/cs/admin";
    }
}
