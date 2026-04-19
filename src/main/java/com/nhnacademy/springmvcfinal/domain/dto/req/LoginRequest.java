package com.nhnacademy.springmvcfinal.domain.dto.req;

import jakarta.validation.constraints.Size;
import lombok.Value;

@Value
public class LoginRequest {
    String id;
    @Size(min = 8, max = 20)
    String pwd;
}
