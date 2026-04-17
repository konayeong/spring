package com.nhnacademy.springmvcfinal.domain.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Value;

@Value
public class LoginRequest {
    @NotBlank
    String id;
    @Size(min = 8, max = 20)
    String pwd;
}
