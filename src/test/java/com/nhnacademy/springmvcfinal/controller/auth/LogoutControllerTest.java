package com.nhnacademy.springmvcfinal.controller.auth;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(LogoutController.class)
class LogoutControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("로그아웃 성공 - 세션 존재")
    void logout_success_with_session() throws Exception {

        mockMvc.perform(post("/cs/logout")
                        .sessionAttr("loginUser", "user"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/cs/login"));
    }

    @Test
    @DisplayName("로그아웃 성공 - 세션 없음")
    void logout_success_without_session() throws Exception {

        mockMvc.perform(post("/cs/logout"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/cs/login"));
    }
}