package com.nhnacademy.springsecurityfinal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;

@SpringBootTest
public class AuthorizationTest {

    private MockMvc mockMvc;

    @BeforeEach
    void setup(WebApplicationContext context) {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    @Test
    @DisplayName("[/admin] ADMIN 성공")
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void admin_200() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/admin"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("[/admin] MEMBER FORBIDDEN")
    @WithMockUser(username = "member", authorities = {"ROLE_MEMBER"})
    public void admin_403() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/admin"))
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    @DisplayName("[/member] MEMBER 성공")
    @WithMockUser(username = "member", roles = {"MEMBER"})
    public void member_200() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/member"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("[/member] GOOGLE FORBIDDEN")
    @WithMockUser(username = "google", roles = {"GOOGLE"})
    public void member_403() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/member"))
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    @DisplayName("[/google] GOOGLE 성공")
    @WithMockUser(username = "google", authorities = {"ROLE_GOOGLE"})
    public void google_200() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/google"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("[/google] ADMIN 실패")
    @WithMockUser(username = "google", authorities = {"ROLE_ADMIN"})
    public void google_403() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/google"))
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

}
