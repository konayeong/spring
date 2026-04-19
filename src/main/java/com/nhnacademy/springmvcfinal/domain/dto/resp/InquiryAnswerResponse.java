package com.nhnacademy.springmvcfinal.domain.dto.resp;

import java.time.LocalDateTime;

public record InquiryAnswerResponse(
        String content,
        LocalDateTime createdAt,
        String responderId
) {
}