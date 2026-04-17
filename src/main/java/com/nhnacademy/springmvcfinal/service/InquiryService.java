package com.nhnacademy.springmvcfinal.service;

import com.nhnacademy.springmvcfinal.domain.inquiry.Category;
import com.nhnacademy.springmvcfinal.domain.inquiry.Inquiry;
import com.nhnacademy.springmvcfinal.domain.dto.InquiryRegisterRequest;
import java.util.List;

public interface InquiryService {
    // user
    List<Inquiry> getInquiryList(String userId, Category category);
    Inquiry getInquiry(int inquiryId);

    void registerInquiry(String userId, InquiryRegisterRequest registerRequest);

    // admin
    List<Inquiry> getInquiryListNonAnswer();
}
