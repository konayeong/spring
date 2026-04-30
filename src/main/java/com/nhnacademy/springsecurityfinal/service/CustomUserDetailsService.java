package com.nhnacademy.springsecurityfinal.service;

import com.nhnacademy.springsecurityfinal.model.AuthUser;
import com.nhnacademy.springsecurityfinal.model.MemberEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final MemberService memberService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        MemberEntity entity = memberService.getMemberEntity(username);
        return new AuthUser(entity);
    }
}
