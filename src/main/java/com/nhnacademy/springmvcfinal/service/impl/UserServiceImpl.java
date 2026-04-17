package com.nhnacademy.springmvcfinal.service.impl;

import com.nhnacademy.springmvcfinal.domain.user.User;
import com.nhnacademy.springmvcfinal.exception.LoginFailedException;
import com.nhnacademy.springmvcfinal.exception.UserNotFoundException;
import com.nhnacademy.springmvcfinal.repository.UserRepository;
import com.nhnacademy.springmvcfinal.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public User doLogin(String id, String password) {
        if(!userRepository.matches(id, password)) {
            throw new LoginFailedException();
        }

        Optional<User> opUser = userRepository.getUser(id);
        if(opUser.isEmpty()) {
            throw new UserNotFoundException(id);
        }
        return opUser.get();
    }
}
