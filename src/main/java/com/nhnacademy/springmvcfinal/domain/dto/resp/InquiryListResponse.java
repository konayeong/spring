package com.nhnacademy.springmvcfinal.domain.dto.resp;

import com.nhnacademy.springmvcfinal.domain.inquiry.Category;
import com.nhnacademy.springmvcfinal.domain.inquiry.Inquiry;

import java.time.LocalDateTime;

public record InquiryListResponse (
        int inquiryId,
        String title,
        String content,
        Category category,
        LocalDateTime createdAt,
        boolean answered
) {
    public static InquiryListResponse from(Inquiry inquiry, boolean answered) {
        return new InquiryListResponse (
                inquiry.getInquiryId(),
                inquiry.getTitle(),
                inquiry.getContent(),
                inquiry.getCategory(),
                inquiry.getCreatedAt(),
                answered
        );
    }
}
