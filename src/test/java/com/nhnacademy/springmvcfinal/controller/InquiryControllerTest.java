package com.nhnacademy.springmvcfinal.controller;

import com.nhnacademy.springmvcfinal.domain.dto.resp.InquiryResponse;
import com.nhnacademy.springmvcfinal.domain.inquiry.Category;
import com.nhnacademy.springmvcfinal.domain.inquiry.Inquiry;
import com.nhnacademy.springmvcfinal.service.InquiryService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import java.time.LocalDateTime;
import java.util.List;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(InquiryController.class)
class InquiryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private InquiryService inquiryService;

    @Test
    void inquiry_detail_success() throws Exception {

        InquiryResponse response = InquiryResponse.from(new Inquiry(
                1, "제목", "내용", Category.COMPLAINT, LocalDateTime.now(), "user1"),
                List.of("file1.png", "file2.png"),null
        );

        when(inquiryService.getInquiry(1)).thenReturn(response);

        mockMvc.perform(get("/cs/inquiries/1")
                .sessionAttr("loginUser", "testUser")) // interceptor 통과
                .andExpect(status().isOk())
                .andExpect(view().name("inquiryDetail"))
                .andExpect(model().attributeExists("inquiry"));
    }
}