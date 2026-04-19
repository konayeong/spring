package com.nhnacademy.springmvcfinal.service.impl;

import com.nhnacademy.springmvcfinal.domain.dto.req.InquiryRegisterRequest;
import com.nhnacademy.springmvcfinal.domain.dto.resp.InquiryListResponse;
import com.nhnacademy.springmvcfinal.domain.dto.resp.InquiryResponse;
import com.nhnacademy.springmvcfinal.domain.inquiry.Answer;
import com.nhnacademy.springmvcfinal.domain.inquiry.Category;
import com.nhnacademy.springmvcfinal.domain.inquiry.Inquiry;
import com.nhnacademy.springmvcfinal.exception.InquiryNotFoundException;
import com.nhnacademy.springmvcfinal.repository.AnswerRepository;
import com.nhnacademy.springmvcfinal.repository.InquiryFileRepository;
import com.nhnacademy.springmvcfinal.repository.InquiryRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class InquiryServiceImplTest {

    @Mock
    InquiryRepository inquiryRepository;

    @Mock
    InquiryFileRepository fileRepository;

    @Mock
    AnswerRepository answerRepository;

    @Mock
    FileService fileService;

    @InjectMocks
    InquiryServiceImpl inquiryService;

    static Inquiry createInquiry() {
        return new Inquiry(1, "제목", "내용", Category.COMPLAINT, LocalDateTime.now(), "user1");
    }

    @Test
    @DisplayName("문의 목록 조회")
    void getInquiryList_success() {
        Inquiry inquiry = createInquiry();

        when(inquiryRepository.getInquiryList("user1", Category.SUGGESTION)).thenReturn(List.of(inquiry));

        when(answerRepository.existsByInquiryId(anyInt())).thenReturn(true);

        List<InquiryListResponse> result = inquiryService.getInquiryList("user1", Category.SUGGESTION);

        assertEquals(1, result.size());
    }

    @Test
    @DisplayName("상세 조회")
    void getInquiry_success() {
        Inquiry inquiry = createInquiry();

        when(inquiryRepository.getInquiry(1)).thenReturn(inquiry);

        when(fileRepository.findByInquiryId(1)).thenReturn(List.of());

        when(answerRepository.findByInquiryId(1)).thenReturn(Optional.empty());

        InquiryResponse result = inquiryService.getInquiry(1);

        assertNotNull(result);
    }

    @Test
    @DisplayName("예외 - 문의 없음")
    void getInquiry_not_found() {
        when(inquiryRepository.getInquiry(1)).thenReturn(null);

        assertThrows(InquiryNotFoundException.class, () -> inquiryService.getInquiry(1));
    }

    @Test
    @DisplayName("예외 - 답변 존재")
    void addAnswer_already_exists() {
        when(inquiryRepository.getInquiry(1)).thenReturn(createInquiry());

        when(answerRepository.existsByInquiryId(1)).thenReturn(true);

        assertThrows(IllegalStateException.class, () -> inquiryService.addAnswer(1, "admin", "답변"));
    }

    @Test
    @DisplayName("답변 성공")
    void addAnswer_success() {
        when(inquiryRepository.getInquiry(1)).thenReturn(createInquiry());

        when(answerRepository.existsByInquiryId(1))
                .thenReturn(false);

        inquiryService.addAnswer(1, "admin", "답변");

        verify(answerRepository).save(any(Answer.class));
    }

    @Test
    @DisplayName("문의등록성공 - 파일없는버전")
    void registerInquiry_success() {
        InquiryRegisterRequest request = new InquiryRegisterRequest("제목", "내용", Category.COMPLAINT);

        when(inquiryRepository.registerInquiry(any())).thenReturn(1);

        inquiryService.registerInquiry("user1", request, List.of());

        verify(inquiryRepository).registerInquiry(any());
    }

    // TODO 파일 있을 때는..?
}