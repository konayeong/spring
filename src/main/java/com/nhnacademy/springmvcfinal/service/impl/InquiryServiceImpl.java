package com.nhnacademy.springmvcfinal.service.impl;

import com.nhnacademy.springmvcfinal.domain.dto.resp.InquiryAnswerResponse;
import com.nhnacademy.springmvcfinal.domain.dto.resp.InquiryListResponse;
import com.nhnacademy.springmvcfinal.domain.dto.resp.InquiryResponse;
import com.nhnacademy.springmvcfinal.domain.inquiry.Answer;
import com.nhnacademy.springmvcfinal.domain.inquiry.Category;
import com.nhnacademy.springmvcfinal.domain.inquiry.Inquiry;
import com.nhnacademy.springmvcfinal.domain.inquiry.InquiryFile;
import com.nhnacademy.springmvcfinal.domain.dto.req.InquiryRegisterRequest;
import com.nhnacademy.springmvcfinal.exception.InquiryNotFoundException;
import com.nhnacademy.springmvcfinal.repository.AnswerRepository;
import com.nhnacademy.springmvcfinal.repository.InquiryFileRepository;
import com.nhnacademy.springmvcfinal.repository.InquiryRepository;
import com.nhnacademy.springmvcfinal.service.InquiryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class InquiryServiceImpl implements InquiryService {

    private final InquiryRepository inquiryRepository;
    private final InquiryFileRepository fileRepository;
    private final AnswerRepository answerRepository;
    private final FileService fileService;

    // 사용자 - 문의 목록
    @Override
    @Transactional(readOnly = true)
    public List<InquiryListResponse> getInquiryList(String userId, Category category) {

        List<Inquiry> inquiries = inquiryRepository.getInquiryList(userId, category);
        List<InquiryListResponse> result = new ArrayList<>();

        for (Inquiry inquiry : inquiries) {
            boolean answered = answerRepository.existsByInquiryId(inquiry.getInquiryId());
            result.add(InquiryListResponse.from(inquiry, answered));
        }

        return result;
    }

    @Override
    @Transactional(readOnly = true)
    public InquiryResponse getInquiry(int inquiryId) {

        Inquiry inquiry = inquiryRepository.getInquiry(inquiryId);

        if (inquiry == null) {
            throw new InquiryNotFoundException(inquiryId);
        }

        List<InquiryFile> files = fileRepository.findByInquiryId(inquiryId);

        List<String> fileNames = files.stream()
                .map(InquiryFile::getFileName)
                .toList();

        InquiryAnswerResponse answerResponse = answerRepository.findByInquiryId(inquiryId)
                .map(answer -> new InquiryAnswerResponse(
                        answer.getContent(),
                        answer.getCreatedAt(),
                        answer.getAdminId()
                ))
                .orElse(null);

        return InquiryResponse.from(inquiry, fileNames, answerResponse);
    }

    @Override
    public void registerInquiry(String userId,
                                InquiryRegisterRequest request,
                                List<MultipartFile> files) {

        Inquiry inquiry = Inquiry.create(
                request.getTitle(),
                request.getContent(),
                request.getCategory(),
                userId
        );

        int inquiryId = inquiryRepository.registerInquiry(inquiry);

        if (files == null) return;

        for (MultipartFile file : files) {
            if (file.isEmpty()) continue;

            validateImage(file);

            String savedFileName = fileService.save(file);

            InquiryFile inquiryFile = InquiryFile.create(
                    inquiryId,
                    savedFileName
            );

            fileRepository.save(inquiryFile);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<InquiryListResponse> getInquiryListNonAnswer() {

        List<Inquiry> inquiries = inquiryRepository.findUnansweredInquiries();

        return inquiries.stream()
                .map(inquiry -> InquiryListResponse.from(inquiry, false))
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public InquiryResponse getInquiryNonAnswer(int inquiryId) {

        Inquiry inquiry = inquiryRepository.getInquiry(inquiryId);

        if (inquiry == null) {
            throw new InquiryNotFoundException(inquiryId);
        }

        // TODO CustomException
        if (answerRepository.existsByInquiryId(inquiryId)) {
            throw new IllegalStateException("이미 답변이 존재합니다.");
        }

        List<InquiryFile> files = fileRepository.findByInquiryId(inquiryId);

        List<String> fileNames = files.stream()
                .map(InquiryFile::getFileName)
                .toList();

        InquiryAnswerResponse answerResponse = answerRepository.findByInquiryId(inquiryId)
                .map(answer -> new InquiryAnswerResponse(
                        answer.getContent(),
                        answer.getCreatedAt(),
                        answer.getAdminId()
                ))
                .orElse(null);

        return InquiryResponse.from(inquiry, fileNames, answerResponse);
    }

    @Override
    public void addAnswer(int inquiryId, String adminId, String content) {
        Inquiry inquiry = inquiryRepository.getInquiry(inquiryId);

        if (inquiry == null) {
            throw new InquiryNotFoundException(inquiryId);
        }

        if (answerRepository.existsByInquiryId(inquiryId)) {
            throw new IllegalStateException("이미 답변이 존재합니다.");
        }

        Answer answer = Answer.create(
                inquiryId,
                adminId,
                content,
                LocalDateTime.now()
        );

        answerRepository.save(answer);
    }

    private void validateImage(MultipartFile file) {
        String contentType = file.getContentType();

        if (contentType == null || !contentType.startsWith("image")) {
            throw new IllegalArgumentException("이미지 파일만 업로드 가능합니다.");
        }
    }
}