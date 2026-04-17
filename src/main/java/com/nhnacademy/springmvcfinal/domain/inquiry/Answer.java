package com.nhnacademy.springmvcfinal.domain.inquiry;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class Answer {
    private final int answerId;
    private final int inquiryId;
    private final int adminId;
    private final String content;
    private final LocalDateTime createdAt;

    // TODO-Q 지금 상황에서는 불필요해보이는데 효과적인 사용 방법
    public static Answer create(int answerId, int inquiryId, int adminId, String content, LocalDateTime createdAt) {
        return new Answer(answerId, inquiryId, adminId, content, createdAt);
    }
}
