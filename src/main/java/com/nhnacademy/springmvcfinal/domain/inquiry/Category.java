package com.nhnacademy.springmvcfinal.domain.inquiry;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Category {
    COMPLAINT(0, "불만 접수"),
    SUGGESTION(1, "제안"),
    REFUND_EXCHANGE(2, "환불/교환"),
    PRAISE(3, "칭찬해요"),
    ETC(4, "기타 문의");

    private final int value;
    private final String description;

    public static Category from(int value) {
        for (Category c : values()) {
            if (c.value == value) {
                return c;
            }
        }
        throw new IllegalArgumentException("Unknown value: " + value);
    }
}
