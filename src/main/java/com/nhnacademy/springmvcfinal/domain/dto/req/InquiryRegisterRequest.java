package com.nhnacademy.springmvcfinal.domain.dto.req;

import com.nhnacademy.springmvcfinal.domain.inquiry.Category;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Value;

@Value
public class InquiryRegisterRequest {
    @Size(min = 2, max = 200)
    String title;
    @Size(max = 40000)
    String content;
    @NotNull
    Category category;
}
