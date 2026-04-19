package com.nhnacademy.springmvcfinal.repository;

import com.nhnacademy.springmvcfinal.domain.inquiry.Answer;

import java.util.Optional;

public interface AnswerRepository {
    boolean existsByInquiryId(int inquiryId);
    Optional<Answer> findByInquiryId(int inquiryId);
    void save(Answer answer);
}
