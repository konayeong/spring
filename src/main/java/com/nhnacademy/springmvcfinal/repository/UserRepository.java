package com.nhnacademy.springmvcfinal.repository;

import com.nhnacademy.springmvcfinal.domain.user.User;

import java.util.Optional;

public interface UserRepository {
    boolean matches(String id, String password);

    Optional<User> getUser(String id);
}
