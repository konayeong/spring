package com.nhnacademy.springmvcfinal.repository;

import com.nhnacademy.springmvcfinal.domain.inquiry.InquiryFile;

import java.util.List;

public interface InquiryFileRepository {
    void save(InquiryFile inquiryFile);
    List<InquiryFile> findByInquiryId(int inquiryId);
}
