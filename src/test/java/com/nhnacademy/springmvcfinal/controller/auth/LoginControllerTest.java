package com.nhnacademy.springmvcfinal.controller.auth;

import com.nhnacademy.springmvcfinal.domain.user.Role;
import com.nhnacademy.springmvcfinal.domain.user.User;
import com.nhnacademy.springmvcfinal.exception.LoginFailedException;
import com.nhnacademy.springmvcfinal.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.MessageSource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import java.util.Locale;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(LoginController.class)
class LoginControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private MessageSource messageSource;

    @Test
    @DisplayName("로그인 성공 - USER")
    void login_success_user() throws Exception {
        User user = User.create("test", "1234qwer", "이름", Role.USER);

        when(userService.doLogin("test", "1234qwer")).thenReturn(user);

        mockMvc.perform(post("/cs/login")
                        .param("id", "test")
                        .param("pwd", "1234qwer"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/cs/"))
                .andExpect(request().sessionAttribute("loginUser", user));
    }

    @Test
    @DisplayName("로그인 성공 - ADMIN")
    void login_success_admin() throws Exception {
        User admin = User.create("admin", "password", "관리자", Role.ADMIN);

        when(userService.doLogin("admin", "password")).thenReturn(admin);

        mockMvc.perform(post("/cs/login")
                        .param("id", "admin")
                        .param("pwd", "password"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/cs/admin"))
                .andExpect(request().sessionAttribute("loginUser", admin));
    }

    @Test
    @DisplayName("로그인 실패 - 비밀번호 오류")
    void login_fail_wrong_password() throws Exception {
        when(userService.doLogin(anyString(), anyString())).thenThrow(new LoginFailedException());

        when(messageSource.getMessage("login.error", null, Locale.KOREA)).thenReturn("로그인 실패");

        mockMvc.perform(post("/cs/login")
                        .param("id", "test")
                        .param("pwd", "wrong123")) // validation 안걸리게
                .andExpect(status().isOk())
                .andExpect(view().name("loginForm"));
    }
}