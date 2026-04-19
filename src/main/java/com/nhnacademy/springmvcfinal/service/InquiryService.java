package com.nhnacademy.springmvcfinal.service;

import com.nhnacademy.springmvcfinal.domain.dto.resp.InquiryListResponse;
import com.nhnacademy.springmvcfinal.domain.dto.resp.InquiryResponse;
import com.nhnacademy.springmvcfinal.domain.inquiry.Category;
import com.nhnacademy.springmvcfinal.domain.dto.req.InquiryRegisterRequest;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface InquiryService {
    // user
    List<InquiryListResponse> getInquiryList(String userId, Category category);
    InquiryResponse getInquiry(int inquiryId);

    void registerInquiry(String userId, InquiryRegisterRequest registerRequest, List<MultipartFile> files);

    // admin
    List<InquiryListResponse> getInquiryListNonAnswer();
    InquiryResponse getInquiryNonAnswer(int inquiryId);
    void addAnswer(int inquiryId, String adminId, String content);
}
