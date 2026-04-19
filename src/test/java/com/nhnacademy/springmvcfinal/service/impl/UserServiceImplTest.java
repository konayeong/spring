package com.nhnacademy.springmvcfinal.service.impl;

import com.nhnacademy.springmvcfinal.domain.user.Role;
import com.nhnacademy.springmvcfinal.domain.user.User;
import com.nhnacademy.springmvcfinal.exception.LoginFailedException;
import com.nhnacademy.springmvcfinal.exception.UserNotFoundException;
import com.nhnacademy.springmvcfinal.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    UserRepository userRepository;

    @InjectMocks
    UserServiceImpl userService;

    @Test
    @DisplayName("로그인성공")
    void doLogin_success() {
        User user = User.create("test", "1234", "이름", Role.USER);

        when(userRepository.matches("test", "1234")).thenReturn(true);

        when(userRepository.getUser("test")).thenReturn(Optional.of(user));

        User result = userService.doLogin("test", "1234");

        assertEquals("test", result.getUserId());
        assertEquals("이름", result.getName());
    }

    @Test
    @DisplayName("로그인 실패 - 비밀번호 오류")
    void doLogin_fail_wrong_password() {
        when(userRepository.matches("test", "wrong")).thenReturn(false);

        assertThrows(LoginFailedException.class, () -> userService.doLogin("test", "wrong"));
    }

    @Test
    @DisplayName("로그인 실패 - 없는 계정")
    void doLogin_user_not_found() {
        when(userRepository.matches("test", "1234")).thenReturn(true);

        when(userRepository.getUser("test")).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.doLogin("test", "1234"));
    }
}