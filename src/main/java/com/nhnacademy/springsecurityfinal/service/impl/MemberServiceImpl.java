package com.nhnacademy.springsecurityfinal.service.impl;

import com.nhnacademy.springsecurityfinal.model.MemberEntity;
import com.nhnacademy.springsecurityfinal.model.dto.MemberCreateRequest;
import com.nhnacademy.springsecurityfinal.model.dto.MemberResponse;
import com.nhnacademy.springsecurityfinal.exception.MemberAlreadyExistsException;
import com.nhnacademy.springsecurityfinal.exception.MemberNotFoundException;
import com.nhnacademy.springsecurityfinal.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final RedisTemplate<String, Object> redisTemplate;
    private final ObjectMapper objectMapper;
    private final PasswordEncoder passwordEncoder;
    private static final String HASH_NAME = "members";

    @Override
    public void createMember(MemberCreateRequest memberCreateRequest) {
        // 아이디 중복확인
        if(redisTemplate.opsForHash().hasKey(HASH_NAME, memberCreateRequest.getId())) {
            throw new MemberAlreadyExistsException(memberCreateRequest.getId());
        }

        // 비밀번호 암호화
        String enPwd = passwordEncoder.encode(memberCreateRequest.getPassword());
        MemberEntity memberEntity = new MemberEntity(memberCreateRequest, enPwd);
        redisTemplate.opsForHash().put(HASH_NAME, memberCreateRequest.getId(), memberEntity);
    }

    @Override
    public MemberResponse getMember(String id) {
        Object o = redisTemplate.opsForHash().get(HASH_NAME, id);
        if(Objects.isNull(o)) {
            throw new MemberNotFoundException(id);
        }

        // TODO-Q MemberEntity - No/AllArgsConstructor 필요한 이유
        MemberEntity memberEntity = objectMapper.convertValue(o, MemberEntity.class);
        return new MemberResponse(memberEntity);
    }

    @Override
    public List<MemberResponse> getMembers() {
        List<MemberResponse> members = new ArrayList<>();
        Map<Object, Object> entries = redisTemplate.opsForHash().entries(HASH_NAME);
        for(Object o : entries.values()) {
            MemberEntity memberEntity = objectMapper.convertValue(o, MemberEntity.class);
            members.add(new MemberResponse(memberEntity));
        }
        return members;
    }

    @Override
    public MemberEntity getMemberEntity(String id) {
        Object o = redisTemplate.opsForHash().get(HASH_NAME, id);
        if(Objects.isNull(o)) {
            throw new MemberNotFoundException(id);
        }

        return objectMapper.convertValue(o, MemberEntity.class);
    }


}
