package com.nhnacademy.springmvcfinal.controller;

import com.nhnacademy.springmvcfinal.domain.inquiry.Category;
import com.nhnacademy.springmvcfinal.domain.user.Role;
import com.nhnacademy.springmvcfinal.domain.user.User;
import com.nhnacademy.springmvcfinal.service.InquiryService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import java.util.List;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserMainController.class)
class UserMainControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private InquiryService inquiryService;

    @Test
    @DisplayName("유저 - 메인")
    void get_inquiries() throws Exception {

        User user = new User("user1", "password", "이름", Role.USER);

        when(inquiryService.getInquiryList("user1", null)).thenReturn(List.of());

        mockMvc.perform(get("/cs")
                        .sessionAttr("loginUser", user))
                .andExpect(status().isOk())
                .andExpect(view().name("userMain"))
                .andExpect(model().attributeExists("name"))
                .andExpect(model().attributeExists("inquiries"))
                .andExpect(model().attributeExists("categories"));
    }

    @Test
    @DisplayName("카테고리 조회 성공")
    void category_get_success() throws Exception {

        User user = new User("user1", "password", "이름", Role.USER);

        when(inquiryService.getInquiryList("user1", Category.COMPLAINT)).thenReturn(List.of());

        mockMvc.perform(get("/cs")
                        .param("category", "COMPLAINT")
                        .sessionAttr("loginUser", user))
                .andExpect(status().isOk())
                .andExpect(view().name("userMain"))
                .andExpect(model().attributeExists("inquiries"));
    }
}