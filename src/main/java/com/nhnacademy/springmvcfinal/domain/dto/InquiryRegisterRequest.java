package com.nhnacademy.springmvcfinal.domain.dto;

import jakarta.validation.constraints.Size;
import lombok.Value;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Value
public class InquiryRegisterRequest {
    @Size(min = 2, max = 200)
    String title;
    @Size(max = 40000)
    String content;
    // TODO-Q 얘는 뭘로 valid
    int category;
    List<MultipartFile> files;
}
