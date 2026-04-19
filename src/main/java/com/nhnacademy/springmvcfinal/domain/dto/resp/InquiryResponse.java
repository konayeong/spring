package com.nhnacademy.springmvcfinal.domain.dto.resp;

import com.nhnacademy.springmvcfinal.domain.inquiry.Category;
import com.nhnacademy.springmvcfinal.domain.inquiry.Inquiry;
import java.time.LocalDateTime;
import java.util.List;

// 문의글 응답 dto
public record InquiryResponse(
        int inquiryId,
        String title,
        String content,
        Category category,
        LocalDateTime createdAt,
        InquiryAnswerResponse answer,
        List<String> files
) {
    public static InquiryResponse from(Inquiry inquiry, List<String> files, InquiryAnswerResponse answer) {
        return new InquiryResponse(
                inquiry.getInquiryId(),
                inquiry.getTitle(),
                inquiry.getContent(),
                inquiry.getCategory(),
                inquiry.getCreatedAt(),
                answer,
                files
        );
    }
}
