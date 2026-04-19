package com.nhnacademy.springmvcfinal.domain.dto.req;

import jakarta.validation.constraints.Size;
import lombok.Value;

@Value
public class AnswerRegisterRequest {
    @Size(min = 1, max = 40000)
    private String content;
}
