package com.nhnacademy.springmvcfinal.repository;

import com.nhnacademy.springmvcfinal.domain.inquiry.Category;
import com.nhnacademy.springmvcfinal.domain.inquiry.Inquiry;

import java.util.List;

public interface InquiryRepository {
    List<Inquiry> getInquiryList(String userId, Category category);
    List<Inquiry> getInquiryListAdmin();
    Inquiry getInquiry(int inquiryId);

    int registerInquiry(Inquiry inquiry);
}
