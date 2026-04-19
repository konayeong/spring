package com.nhnacademy.springmvcfinal.domain.inquiry;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor // 조회용
public class Answer {
    @Setter
    private int answerId;
    private final int inquiryId;
    private final String adminId;
    private final String content;
    private final LocalDateTime createdAt;

    private Answer(int inquiryId, String adminId, String content, LocalDateTime createdAt) {
        this.inquiryId = inquiryId;
        this.adminId = adminId;
        this.content = content;
        this.createdAt = createdAt;
    }

    // TODO-Q 지금 상황에서는 불필요해보이는데 효과적인 사용 방법
    public static Answer create(int inquiryId, String adminId, String content, LocalDateTime createdAt) {
        return new Answer(inquiryId, adminId, content, createdAt);
    }
}
