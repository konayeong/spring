package com.nhnacademy.springmvcfinal.domain.inquiry;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
public class Inquiry {
    @Setter // TODO autoincrement 때문에 이렇게 했는데 다른 방법
    private int inquiryId;
    private final String title;
    private final String content;
    private final Category category;
    private final LocalDateTime createdAt;
    private final String userId;

    // DB 조회용
    public Inquiry(int inquiryId, String title, String content, Category category, LocalDateTime createdAt, String userId) {
        this.inquiryId = inquiryId;
        this.title = title;
        this.content = content;
        this.category = category;
        this.createdAt = createdAt;
        this.userId = userId;
    }

    // 생성용
    private Inquiry(String title, String content, Category category, LocalDateTime createdAt, String userId) {
        this.title = title;
        this.content = content;
        this.category = category;
        this.createdAt = createdAt;
        this.userId = userId;
    }

    public static Inquiry create(String title, String content, Category category, String userId) {
        return new Inquiry(title, content, category, LocalDateTime.now(), userId);
    }
}
