package com.nhnacademy.springmvcfinal.controller.admin;

import com.nhnacademy.springmvcfinal.domain.dto.resp.InquiryResponse;
import com.nhnacademy.springmvcfinal.domain.user.Role;
import com.nhnacademy.springmvcfinal.domain.user.User;
import com.nhnacademy.springmvcfinal.service.InquiryService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import java.util.List;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AdminController.class)
class AdminControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private InquiryService inquiryService;

    @Test
    @DisplayName("관리자 메인 - 문의글 조회")
    void admin_main() throws Exception {

        when(inquiryService.getInquiryListNonAnswer()).thenReturn(List.of());

        mockMvc.perform(get("/cs/admin")
                .sessionAttr("loginUser", "pass"))
                .andExpect(status().isOk())
                .andExpect(view().name("adminMain"))
                .andExpect(model().attributeExists("inquiries"));
    }

    @Test
    @DisplayName("답변 폼 조회")
    void answer_form() throws Exception {

        InquiryResponse response = Mockito.mock(InquiryResponse.class);

        when(inquiryService.getInquiryNonAnswer(1)).thenReturn(response);

        mockMvc.perform(get("/cs/admin/answer/1")
                .sessionAttr("loginUser", "pass"))
                .andExpect(status().isOk())
                .andExpect(view().name("adminAnswerForm"))
                .andExpect(model().attributeExists("inquiry"));
    }

    @Test
    @DisplayName("답변등록성공")
    void answer_register_success() throws Exception {
        User user = new User("admin1", "password", "관리자", Role.ADMIN);

        mockMvc.perform(post("/cs/admin/answer/1")
                    .sessionAttr("loginUser", user)
                    .param("content", "답변입니다"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/cs/admin"));
    }
}


