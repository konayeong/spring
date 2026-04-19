package com.nhnacademy.springmvcfinal.controller;

import com.nhnacademy.springmvcfinal.domain.user.Role;
import com.nhnacademy.springmvcfinal.domain.user.User;
import com.nhnacademy.springmvcfinal.service.InquiryService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(InquiryRegisterController.class)
class InquiryRegisterControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private InquiryService inquiryService;

    @Test
    @DisplayName("문의등록 폼 조회 성공")
    void inquiryRegister_form() throws Exception {
        mockMvc.perform(get("/cs/inquiry")
                .sessionAttr("loginUser", "user"))
                .andExpect(status().isOk())
                .andExpect(view().name("inquiryRegister"))
                .andExpect(model().attributeExists("categories"));
    }

    @Test
    @DisplayName("문의등록 성공")
    void inquiryRegister_success() throws Exception {

        User user = new User("user1", "1234qwer", "유저", Role.USER);

        MockMultipartFile file1 = new MockMultipartFile("files", "a.png", "image/png", "img1".getBytes());

        MockMultipartFile file2 = new MockMultipartFile("files", "b.png", "image/png", "img2".getBytes());

        mockMvc.perform(multipart("/cs/inquiry")
                        .file(file1)
                        .file(file2)
                        .sessionAttr("loginUser", user)
                        .param("title", "제목")
                        .param("content", "내용")
                        .param("category", "COMPLAINT"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/cs/"));
    }
}