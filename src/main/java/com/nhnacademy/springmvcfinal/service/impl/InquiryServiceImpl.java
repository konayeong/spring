package com.nhnacademy.springmvcfinal.service.impl;

import com.nhnacademy.springmvcfinal.domain.inquiry.Category;
import com.nhnacademy.springmvcfinal.domain.inquiry.Inquiry;
import com.nhnacademy.springmvcfinal.domain.inquiry.InquiryFile;
import com.nhnacademy.springmvcfinal.domain.dto.InquiryRegisterRequest;
import com.nhnacademy.springmvcfinal.exception.InquiryNotFoundException;
import com.nhnacademy.springmvcfinal.repository.InquiryFileRepository;
import com.nhnacademy.springmvcfinal.repository.InquiryRepository;
import com.nhnacademy.springmvcfinal.service.InquiryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class InquiryServiceImpl implements InquiryService {

    private final InquiryRepository inquiryRepository;
    private final InquiryFileRepository fileRepository;

    // TODO-Q user 존재 확인 필요성
    @Override
    @Transactional(readOnly = true)
    public List<Inquiry> getInquiryList(String userId, Category category) {
        // inquires == null -> 문의 내역이 없습니다.
        return inquiryRepository.getInquiryList(userId, category);

    }

    @Override
    public Inquiry getInquiry(int inquiryId) {
        Inquiry inquiry = inquiryRepository.getInquiry(inquiryId);
        if(inquiry == null) {
            throw new InquiryNotFoundException(inquiryId);
        }
        return inquiry;
    }

    @Override
    public void registerInquiry(String userId, InquiryRegisterRequest registerRequest) {
        // TODO build ... 쓸까
        Inquiry registerInquiry = Inquiry.create(
                registerRequest.getTitle(),
                registerRequest.getContent(),
                Category.from(registerRequest.getCategory()),
                userId
        );
        int inquiryId = inquiryRepository.registerInquiry(registerInquiry);

        List<MultipartFile> files = registerRequest.getFiles();

        if (files != null) {
            for (MultipartFile file : files) {
                if (file.isEmpty()) continue;

                validateImage(file); // 확장자 검증
                String savedFileName = saveFile(file);

                InquiryFile inquiryFile = InquiryFile.create(
                        inquiryId,
                        savedFileName
                );

                fileRepository.save(inquiryFile);
            }
        }
    }

    @Override
    public List<Inquiry> getInquiryListNonAnswer() {
        // TODO-first : 관리자 메인페이지 (답변 없는 문의 목록) - 답변 여부 정보는 어디서 ? domain 필드 추가 ?
        return List.of();
    }

    private void validateImage(MultipartFile file) {
        String contentType = file.getContentType();

        if (contentType == null || !(contentType.startsWith("image"))) {
            throw new IllegalArgumentException("이미지 파일만 업로드 가능합니다.");
        }
    }

    private String saveFile(MultipartFile file) {
        try {
            // TODO 경로 수정 (임시)
            String uploadDir = System.getProperty("user.dir") + "/uploads/";
            String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();

            Path path = Paths.get(uploadDir + fileName);
            System.out.println(path);
            Files.copy(file.getInputStream(), path);

            return fileName;

        } catch (IOException e) {
            throw new RuntimeException("파일 저장 실패", e);
        }
    }
}
