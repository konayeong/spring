package com.nhnacademy.springsecurityfinal.model;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Arrays;
import java.util.Collection;
import java.util.Map;

public class AuthUser implements UserDetails, OAuth2User {

    private final String username;
    private final String password;
    private final Role role;

    public AuthUser(MemberEntity memberEntity) {
        this.username = memberEntity.getId();
        this.password = memberEntity.getPassword();
        this.role = memberEntity.getRole();
    }

    public AuthUser(OAuth2User oAuth2User) {
        this.username = oAuth2User.getAttribute("email");
        this.password = null;
        this.role = Role.GOOGLE;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Arrays.asList(new SimpleGrantedAuthority("ROLE_" + role.name()));
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    // OAuth2
    @Override
    public String getName() {
        return username;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return Map.of();
    }
}

